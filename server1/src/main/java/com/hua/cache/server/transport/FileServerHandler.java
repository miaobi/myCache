package com.hua.cache.server.transport;

import com.hua.cache.common.cacheProtocol.Msg;
import com.hua.cache.common.cacheProtocol.MsgType;
import com.hua.cache.common.cacheProtocol.ShortMsg;
import com.hua.cache.common.selfProtocol.ProtocolType;
import com.hua.cache.common.selfProtocol.SmartCarProtocol;
import com.hua.cache.server.util.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

import java.io.RandomAccessFile;


public class FileServerHandler extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof ShortMsg){
            ShortMsg in = (ShortMsg)msg;
            int inType = in.getType();
            if(MsgType.INDEX_REQ.value() == inType){
                RandomAccessFile raf = new RandomAccessFile(Constants.FILE_NAME, "r");
                int length = (int)raf.length();
                if(raf!=null && length>0){
                    int type = MsgType.INDEX_RSP.value();
                    Msg headMsg = new Msg(type, length);
                    ByteBuf out = headMsg.encode();
                    ctx.writeAndFlush(out);
                    ctx.writeAndFlush(new DefaultFileRegion(raf.getChannel(), 0, length));
                }
            }
            if(MsgType.LODE_REQ.value() == inType){
                byte[] content = Constants.FILE_NAME.getBytes(CharsetUtil.UTF_8);
                ShortMsg out = new ShortMsg(MsgType.LODE_RSP.value(), content.length, content);
                ctx.writeAndFlush(out);
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            ctx.writeAndFlush("ERR: " +
                    cause.getClass().getSimpleName() + ": " +
                    cause.getMessage() + '\n').addListener(ChannelFutureListener.CLOSE);
        }
    }
}

