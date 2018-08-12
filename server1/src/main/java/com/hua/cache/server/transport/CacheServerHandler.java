package com.hua.cache.server.transport;

import com.hua.cache.server.data.model.Category;
import com.hua.cache.server.util.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.util.List;

public class CacheServerHandler extends ChannelInboundHandlerAdapter {
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
        ByteBuf bb = (ByteBuf)msg;
        String str = bb.toString(CharsetUtil.UTF_8);
//        String[] strings = str.split("\\" + Constants.SPLIT);
//        if(strings.length>=3){
//            String id = strings[0];
//            int type = Integer.parseInt(strings[1]);
//            Cache<Integer,List<Category>> cache = (Cache) ctx.attr(AttributeKey.valueOf(ctx.channel().id().asLongText())).get();
//            if(Constants.TYPE_CACHE_PUT == type){
//                String key = strings[2];
//                String value = strings[3];
//                cache.put(key,value);
//                String retMsg = new StringBuffer()
//                        .append(id)
//                        .append(Constants.SPLIT)
//                        .append(Constants.TYPE_CACHE_PUT)
//                        .append(Constants.SPLIT)
//                        .append(true)
//                        .toString();
//                ByteBuf retBB = Unpooled.copiedBuffer(retMsg, CharsetUtil.UTF_8);
//                ctx.channel().writeAndFlush(retBB);
//            }else if(Constants.TYPE_CACHE_GET == type){
//                String key = strings[2];
//                Object value = cache.get(key);
//                String strValue = JSONUtils.toJSONString(value);
//                String retMsg = new StringBuffer()
//                        .append(id)
//                        .append(Constants.SPLIT)
//                        .append(Constants.TYPE_CACHE_GET)
//                        .append(Constants.SPLIT)
//                        .append(strValue)
//                        .toString();
//                System.out.println("retMsg:"+retMsg);
//                ByteBuf retBB = Unpooled.copiedBuffer(retMsg, CharsetUtil.UTF_8);
//                ctx.writeAndFlush(retBB);
//            }
//        }
//        System.out.println(str);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete remoteAddress:"+ctx.channel().remoteAddress() +"-->localAddress:"+ctx.channel().localAddress());
        super.channelReadComplete(ctx);
    }
}
