package com.sheng.hua.cache.impl;

import com.hua.cache.common.selfProtocol.ProtocolType;
import com.hua.cache.common.selfProtocol.SmartCarProtocol;
import com.sheng.hua.cache.data.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.SocketAddress;
import java.nio.channels.FileChannel;


@ChannelHandler.Sharable
public class ProtocolHandler extends ChannelDuplexHandler {
    private final FileChannel fc;
    private final int length;
    public ProtocolHandler() throws IOException {
        File file = new File(Constants.FILE_NAME);
        if (!file.exists()) {
            file.createNewFile();
        }
        RandomAccessFile aFile = new RandomAccessFile(file, "rw");
        length = (int)aFile.length();
        fc = aFile.getChannel();
    }


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
        ByteBuf content = Unpooled.wrappedBuffer(Constants.FILE_NAME.getBytes(CharsetUtil.UTF_8));
        SmartCarProtocol msg = new SmartCarProtocol(ProtocolType.INDEX_REQ.value(), content.readableBytes(), content);
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
        if(msg instanceof SmartCarProtocol) {
            SmartCarProtocol in = (SmartCarProtocol) msg;
        }
        if(msg instanceof ByteBuf){
            ByteBuf bb = (ByteBuf)msg;
            if(bb.readInt() == SmartCarProtocol.HEAD_DATA){
                int type = bb.readInt();
                int length = bb.readInt();
                if(ProtocolType.INDEX_RSP.value() == type){
                    int offset = 0;
                    int subSize = 0;
                    while(bb.isReadable()) {
                        subSize = bb.readableBytes();
                        if(offset + subSize < length){
                            bb.readBytes(fc,offset,subSize);
                            offset+=subSize;
                        }else{
                            bb.readBytes(fc,offset,length-offset);
                            break;
                        }
                    }
                }

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
