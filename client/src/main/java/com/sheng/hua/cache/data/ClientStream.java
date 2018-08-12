package com.sheng.hua.cache.data;

import com.hua.cache.common.Message;
import com.hua.cache.common.impl.GetMessage;
import com.hua.cache.common.impl.PutMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientStream {
    private Channel channel;
    private final Map<String,String> resultMap = new ConcurrentHashMap<>();

    public Channel getChannel() {
        return channel;
    }

    public Map<String,String> getResultMap(){
        return resultMap;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void writeAndFlush(Object o){
        channel.writeAndFlush(o);
    }

    public void read(Object o){
        channel.read();
    }

    public boolean acquirePutReslut(Object key, Object vaule) throws Exception{
        Object v = null;
        String id = key.toString() + vaule.toString() + String.valueOf(System.currentTimeMillis());
        try{
//            String msg = new StringBuffer()
//                    .append(id)
//                    .append(Constants.SPLIT)
//                    .append(Constants.TYPE_CACHE_PUT)
//                    .append(Constants.SPLIT)
//                    .append(key)
//                    .append(Constants.SPLIT)
//                    .append(vaule)
//                    .toString();
//            ByteBuf bb = Unpooled.wrappedBuffer(msg.getBytes(CharsetUtil.UTF_8));
            Message message = new PutMessage().id(id).key(key).value(vaule);
            ByteBuf bb = message.encode();
            channel.writeAndFlush(bb);
            v = this.getResult(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Object acquireGetReslut(Object key) throws Exception {
        Object v = null;
        String id = key.toString() + String.valueOf(System.currentTimeMillis());
        try {
        Message getMsg = new GetMessage().key(key);
//            String msg = new StringBuffer()
//                    .append(id)
//                    .append(Constants.SPLIT)
//                    .append(Constants.TYPE_CACHE_GET)
//                    .append(Constants.SPLIT)
//                    .append(key)
//                    .toString();
//            ByteBuf bb = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);
            ByteBuf bb = getMsg.encode();
            channel.writeAndFlush(bb);
//            long time = 0;
//            for (; ; ) {
//                v = resultMap.get(id);
//                if (v != null) {
//                    break;
//                }
//                if (time >= 3000) {
//                    throw new Exception("timeout");
//                }
//                Thread.sleep(100);
//                time += 100;
//            }
            v = this.getResult(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return v;
    }

    public Object getResult(String id) throws Exception {
        Object v = null;
        long time = 0;
        for (;;){
            v = resultMap.get(id);
            if(v!=null){
                break;
            }
            if(time>=3000){
                throw new Exception("timeout");
            }
            Thread.sleep(100);
            time+=100;
        }
        return v;
    }

}
