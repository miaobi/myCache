package com.sheng.hua.cache.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ChannelHandlerContextUtil<T>{
    private final List<ClientStream> list = new ArrayList<>();
    private final Map<String,ClientStream> channelHandlerContextMap = new ConcurrentHashMap<>();
    private static ChannelHandlerContextUtil channelHandlerContextUtil;

    public static ChannelHandlerContextUtil getInstance() {
        if(channelHandlerContextUtil==null){
            synchronized (ChannelHandlerContextUtil.class){
                if(channelHandlerContextUtil==null){
                    channelHandlerContextUtil = new ChannelHandlerContextUtil();
                }
            }
        }
        return channelHandlerContextUtil;
    }

    public Map<String,ClientStream> getClientStreamMap(){
        return channelHandlerContextMap;
    }

    public ClientStream getClientStream(Integer index){
        return list.get(index);
    }

    public void putClientStream(String key,ClientStream clientStream){
        channelHandlerContextMap.put(key,clientStream);
        list.add(clientStream);
    }

    public int size(){
        return list.size();
    }

}
