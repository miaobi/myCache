package com.sheng.hua.cache.impl;

import com.sheng.hua.cache.api.CacheClient;
import com.sheng.hua.cache.data.ServerInetAddress;
import com.sheng.hua.cache.data.ChannelHandlerContextUtil;
import com.sheng.hua.cache.data.ClientStream;
import io.netty.channel.Channel;

public class CacheClientImpl implements CacheClient {
    private ChannelHandlerContextUtil channelHandlerContextUtil = ChannelHandlerContextUtil.getInstance();

    public Channel connect(ServerInetAddress serverInetAddress){
        Channel channel = null;
        try {
            channel = NettyClient.start(serverInetAddress.getHost(),serverInetAddress.getPort());
            ClientStream clientStream = new ClientStream();
            clientStream.setChannel(channel);
            channelHandlerContextUtil.putClientStream(serverInetAddress.toString(),clientStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }

    public boolean put(Object key, Object vaule) throws Exception {
        int size = channelHandlerContextUtil.size();
        int index = key.hashCode() % size;
        ClientStream clientStream = channelHandlerContextUtil.getClientStream(index);
        return clientStream.acquirePutReslut(key, vaule);
    }

    public Object get(Object key) throws Exception {
        int size = channelHandlerContextUtil.size();
        int index = key.hashCode() % size;
        ClientStream clientStream = channelHandlerContextUtil.getClientStream(index);
        return clientStream.acquireGetReslut(key);
    }
}
