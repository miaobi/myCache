package com.sheng.hua.cache.impl;


import com.hua.cache.common.cacheProtocol.LargeMsg;
import com.hua.cache.common.cacheProtocol.MsgType;
import com.hua.cache.common.cacheProtocol.ShortMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;


public class CacheCodec extends ByteToMessageCodec<ShortMsg> {
    //基本长度至少包含消息类型(int类型)、消息长度（int 类型）的总长度
    private static final int BASE_LENGTH = 4 + 4;
    //标记是否是新消息
    private static boolean isFirst = true;
    //消息已读长度
    private static int readLength = 0;
    //消息类型
    private static int type;
    //消息长度
    private static int length;

    @Override
    protected void encode(ChannelHandlerContext ctx, ShortMsg msg, ByteBuf out) throws Exception {
        //写入消息类型(int类型)
        out.writeInt(msg.getType());
        //写入消息的长度(int 类型)
        out.writeInt(msg.getLength());
        //写入消息的内容(byte[] 类型)
        if(msg.getLength()>0 && msg.getContent() != null) {
            out.writeBytes(msg.getContent());
        }
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(isFirst == true){
            if (in.readableBytes() >= BASE_LENGTH) {
                int beginReader = in.readerIndex();
                //数据类型
                type = in.readInt();
                //数据内容总长度
                length = in.readInt();
                int test = in.readableBytes();
                if(type == MsgType.INDEX_RSP.value()){
                    //如果是文件数据，则到了多少数据就读多少数据
                    subDecode(in, out);
                }else if(in.readableBytes() < length){
                    //如果是短消息且数据未到齐，则还原读指针等待数据到齐再一次性读取
                    in.readerIndex(beginReader);
                    return;
                }else {
                    byte[] bytes = new byte[length];
                    in.readBytes(bytes, 0,length);
                    ShortMsg msg = new ShortMsg(type, length, bytes);
                    out.add(msg);
                    return;
                }

            }
        }else {
            //数据在前面已经读取一部分
            subDecode(in, out);
        }
    }

    public void subDecode(ByteBuf in, List<Object> out){
        int readableLength = in.readableBytes();
        if(readableLength < length - readLength){
            //数据未完整到齐
            isFirst = false;
            byte[] bytes = new byte[readableLength];
            in.readBytes(bytes, 0, readableLength);
            //将上次的已读长度作为写入文件的起始位置
            LargeMsg msg = new LargeMsg(type, length, readLength, bytes);
            out.add(msg);
            readLength += readableLength;
        }else {
            //数据已到齐
            byte[] bytes = new byte[length - readLength];
            in.readBytes(bytes, 0,length - readLength);
            //将上次的已读长度作为写入文件的起始位置
            LargeMsg msg = new LargeMsg(type, length, readLength, bytes);
            out.add(msg);
            //消息已读取完毕，恢复原始设置
            readLength = 0;
            isFirst = true;
        }
    }

}