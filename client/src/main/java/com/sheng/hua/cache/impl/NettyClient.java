package com.sheng.hua.cache.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

public class NettyClient {
    private static EventLoopGroup group;

    public static Channel start(String host, int port) throws Exception{
        group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception{
                            ch.pipeline().addLast(new CacheCodec());
                            ch.pipeline().addLast(new CacheHandler());
//                            ch.pipeline().addLast(new NewCacheClientHandler());
                        }
                    }).option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.TCP_NODELAY,true) ;
            ChannelFuture f = b.connect().sync();
//            f.channel().closeFuture().sync();
            return f.channel();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public static void shutdown() throws Exception{
        try{
            if(group!=null){
                group.shutdownGracefully().sync();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) throws Exception{
        String host = "127.0.0.1";
        int port = 8090;
        NettyClient.start(host, port);
    }
}
