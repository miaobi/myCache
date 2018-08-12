package com.hua.cache.server.transport;

import com.hua.cache.common.Message;
import com.hua.cache.common.impl.FileListMessage;
import com.hua.cache.common.impl.FileWriteMessage;
import com.hua.cache.common.impl.GetMessage;
import com.hua.cache.common.impl.PutMessage;
import com.hua.cache.common.util.MessageFactory;
import com.hua.cache.server.util.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultFileRegion;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class NewCacheServerHandler extends ChannelInboundHandlerAdapter {
    private final RandomAccessFile raf;

    public NewCacheServerHandler() throws FileNotFoundException {
        raf = new RandomAccessFile(Constants.FILE_NAME, "r");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
//        Cache<Integer,List<Category>> cache = new CatCacheImpl<Integer,List<Category>>();
//        cache.start();
//        ctx.attr(AttributeKey.valueOf(ctx.channel().id().asLongText())).setIfAbsent(cache);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        try{
            if(msg instanceof ByteBuf){
                ByteBuf bb = (ByteBuf)msg;
                Message message = MessageFactory.createMsgBy(bb);
                message.decode(bb);
                if(message instanceof PutMessage){
                    PutMessage put = (PutMessage)message;
                    put.getId();

                }else if(message instanceof GetMessage){
                    GetMessage put = (GetMessage)message;
                    put.getId();
                }else if(message instanceof FileListMessage){
                    FileListMessage flm = (FileListMessage)message;
                    String[] files = new String[1];
                    int[] lengths = new int[1];
                    files[0] = Constants.FILE_NAME;
                    lengths[0] = 100;
                    flm.files(files);
                    flm.lengths(lengths);
                    ByteBuf retBB = flm.encode();
                    ctx.writeAndFlush(retBB);
                }else if(message instanceof FileWriteMessage){
                    FileWriteMessage fwm = (FileWriteMessage)message;
                    int fileName = fwm.getFileName();
                    if(raf==null){
                        return;
                    }
                    int length=(int)raf.length();
                    Message retMeg = new FileWriteMessage().fileName(1).fileLength(length).version(1);
                    ByteBuf retBB = retMeg.encode();
                    ctx.writeAndFlush(retBB);
                    ctx.writeAndFlush(new DefaultFileRegion(raf.getChannel(), 0, length));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        super.channelReadComplete(ctx);
    }
}
