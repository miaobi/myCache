package com.sheng.hua.cache.impl;

import com.hua.cache.common.cacheProtocol.LargeMsg;
import com.hua.cache.common.cacheProtocol.Msg;
import com.hua.cache.common.cacheProtocol.MsgType;
import com.hua.cache.common.cacheProtocol.ShortMsg;
import com.hua.cache.common.selfProtocol.ProtocolType;
import com.hua.cache.common.selfProtocol.SmartCarProtocol;
import com.sheng.hua.cache.data.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.SocketAddress;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;


@ChannelHandler.Sharable
public class CacheHandler extends ChannelDuplexHandler {
    private static FileChannel fc = null;
    private static int length = 0;


    /////////////////////////////////////////////////////////
    ///  Outbound
    /////////////////////////////////////////////////////////
    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        System.out.println("bind remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        super.bind(ctx, localAddress, promise);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        System.out.println("connect remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        super.connect(ctx, remoteAddress, localAddress, promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("disconnect remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
//        if(fc!=null){
//            fc.close();
//        }
//        super.disconnect(ctx, promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("close remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        super.close(ctx, promise);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("deregister remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        super.deregister(ctx, promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        System.out.println("read remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        super.read(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("write remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        super.write(ctx, msg, promise);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        System.out.println("flush remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        super.flush(ctx);
    }

    /////////////////////////////////////////////////////////
    ///  Inbound
    /////////////////////////////////////////////////////////
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        ShortMsg msg = new ShortMsg(MsgType.LODE_REQ.value(), 0, null);
        ctx.writeAndFlush(msg);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        super.channelInactive(ctx);
    }
    private static final AttributeKey key = AttributeKey.newInstance("readFile");
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        if(msg instanceof ShortMsg) {
            ShortMsg in = (ShortMsg) msg;
            if(in.getType() == MsgType.LODE_RSP.value()) {
                byte[] content = in.getContent();
                String serverFileName =  new String(content, "UTF-8");
                if(serverFileName.endsWith(new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".index")){
                    File file = new File(Constants.FILE_NAME);
                    if (!file.exists()) {
                        file.createNewFile();
                        RandomAccessFile aFile = new RandomAccessFile(file, "rw");
                        length = (int)aFile.length();
                        fc = aFile.getChannel();
                        ShortMsg out = new ShortMsg(MsgType.INDEX_REQ.value(), content.length, content);
                        ctx.writeAndFlush(out);
                    }
                }
            }
        }
        if(msg instanceof LargeMsg) {
            LargeMsg in = (LargeMsg) msg;
            if(in.getType() == MsgType.INDEX_RSP.value()){
                byte[] content = in.getContent();
                Unpooled.wrappedBuffer(content).readBytes(fc,in.getReadLength(), content.length);
            }
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        super.channelReadComplete(ctx);
    }
}
