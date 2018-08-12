package com.sheng.hua.cache.impl;


import com.hua.cache.common.selfProtocol.ProtocolType;
import com.hua.cache.common.selfProtocol.SmartCarProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;


public class ProtocolCodec extends MessageToByteEncoder<SmartCarProtocol> {
    //基本长度至少包含开头标志(int类型)、消息类型(int类型)、消息长度（int 类型）的总长度
    public final int BASE_LENGTH = 4 + 4 + 4;

    @Override
    protected void encode(ChannelHandlerContext ctx, SmartCarProtocol msg, ByteBuf out) throws Exception {
        // 1.写入消息的开头的信息标志(int类型)
        out.writeInt(msg.getHead_data());
        // 2.写入消息类型(int类型)
        out.writeInt(msg.getType());
        // 3.写入消息的长度(int 类型)
        out.writeInt(msg.getContentLength());
        // 4.写入消息的内容(ByteBuf类型)
        if(msg.getContentLength()>0) {
            out.writeBytes(msg.getContent());
        }
    }


//    @Override
//    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        // 可读长度必须大于基本长度
//        if (in.readableBytes() >= BASE_LENGTH) {
////            // 防止socket字节流攻击
////            // 防止，客户端传来的数据过大
////            // 因为，太大的数据，是不合理的
////            if (in.readableBytes() > 2048) {
////                in.skipBytes(in.readableBytes());
////            }
//
//            // 记录包头开始的index
//            int beginReader;
//
//            while (true) {
//                // 获取包头开始的index
//                beginReader = in.readerIndex();
//                // 标记包头开始的index
//                in.markReaderIndex();
//                // 读到了协议的开始标志，结束while循环
//                int test = in.readInt();
//                if (test == SmartCarProtocol.HEAD_DATA) {
//                    break;
//                }
//
//                // 未读到包头，略过一个字节
//                // 每次略过一个字节去读取，包头信息的开始标记
//                in.resetReaderIndex();
//                in.readByte();
//
//                // 当略过，一个字节之后，
//                // 数据包的长度，又变得不满足
//                // 此时，应该结束。等待后面的数据到达
//                if (in.readableBytes() < BASE_LENGTH) {
//                    return;
//                }
//            }
//            // 消息类型
//            int type = in.readInt();
//            // 消息的长度
//            int length = in.readInt();
//
//            //判断数据包数据是否到齐
//            if(ProtocolType.LODE_RSP.value() == type || ProtocolType.INDEX_RSP.value() == type){
//                //如果是文件数据则不采用此种解码方式，还原读指针
//                in.readerIndex(beginReader);
//                return;
//            }else {
//                //如果是普通数据则重试
//                if (in.readableBytes() < length) {
//                    // 还原读指针
//                    in.readerIndex(beginReader);
//                    return;
//                }
//            }
//
////            // 读取data数据
////            byte[] data = new byte[length];
////            in.readBytes(data);
//
//            //读取数据内容
//            ByteBuf data = in.readSlice(length);
//
//            SmartCarProtocol protocol = new SmartCarProtocol(type, length, data);
//            out.add(protocol);
//        }
//    }

}