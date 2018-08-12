package com.hua.cache.server.transport;

import com.hua.cache.server.data.model.Category;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class CacheServer {
    private EventLoopGroup boss;
    private EventLoopGroup work;

    public static void main(String[] args) throws Exception {
        int port = 8090;
        for (int i = 0; i < 3; i++,port++) {
            new CacheServer().start(port);
            System.out.println("port:" + port);
        }
    }

    public Channel start(int port) throws Exception{
        boss = new NioEventLoopGroup();
        work = new NioEventLoopGroup();
        Channel channel = null;
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss,work)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception{
                            ch.pipeline().addLast(new CacheServerHandler());
                        }
                    });
            ChannelFuture f = b.bind().sync();
            channel = f.channel();
//            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return channel;
    }

    public void shutdown() throws Exception{
        try{
            if(boss!=null){
                boss.shutdownGracefully().sync();
            }
            if(work!=null){
                work.shutdownGracefully().sync();
            }
        }catch (Exception e){
           e.printStackTrace();
           throw e;
        }
    }
}
