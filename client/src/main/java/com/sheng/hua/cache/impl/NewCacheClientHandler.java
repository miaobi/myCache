package com.sheng.hua.cache.impl;

import com.hua.cache.common.Message;
import com.hua.cache.common.impl.FileListMessage;
import com.hua.cache.common.impl.FileWriteMessage;
import com.hua.cache.common.util.MessageFactory;
import com.sheng.hua.cache.data.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.SocketAddress;
import java.nio.channels.FileChannel;

@ChannelHandler.Sharable
public class NewCacheClientHandler extends ChannelDuplexHandler {
    private final FileChannel fc;
    private final int length;
    public NewCacheClientHandler() throws IOException {
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
        if(fc!=null){
            fc.close();
        }
        super.disconnect(ctx, promise);
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
        if(length<=0){
            Message message = new FileListMessage();
            ByteBuf bb = message.encode();
            ctx.writeAndFlush(bb);
        }
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        super.channelInactive(ctx);
    }
    private static final AttributeKey FILE_LENGTH_KEY = AttributeKey.newInstance("fileLength");
    private static final AttributeKey FILE_STATE_KEY = AttributeKey.newInstance("fileState");
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        System.out.println(msg);
        if(msg instanceof ByteBuf){
            ByteBuf bb = (ByteBuf)msg;
            Object state = ctx.attr(FILE_STATE_KEY).get();
            if(state==null || (int)state!=0){
                Message message = MessageFactory.createMsgBy(bb);
                message.decode(bb);
                if(message instanceof FileListMessage){
                    FileListMessage flm = (FileListMessage)message;
                    String[] files = flm.getFiles();
                    Message fwMsg = new FileWriteMessage();
                    ByteBuf reqMsg = fwMsg.encode();
                    ctx.writeAndFlush(reqMsg);
                }else if(message instanceof FileWriteMessage){
                    FileWriteMessage flm = (FileWriteMessage)message;
                    int length = flm.getFileLength();
                    int fileName = flm.getFileName();
                    int version = flm.getVersion();
                    ctx.attr(FILE_STATE_KEY).set(0);
                }
            }else {
                try {
                    Object o = ctx.attr(FILE_LENGTH_KEY).get();
                    int offset = o==null?0:(int)o;
                    while(bb.isReadable()) {
                        int length = bb.readableBytes();
                        bb.readBytes(fc,offset,length);
                        offset+=length;
                        ctx.attr(FILE_LENGTH_KEY).set(offset);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
