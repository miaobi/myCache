package com.hua.cache.common.impl;

import com.hua.cache.common.Message;
import com.hua.cache.common.MessageType;
import com.hua.cache.common.util.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class PutMessage extends HeadMessage implements Message {
    public PutMessage(){
        super.setType(MessageType.PUT.value());
    }

    public PutMessage id(String id){
        this.id = id;
        return this;
    }
    public PutMessage key(Object key){
        this.key = key;
        return this;
    }
    public PutMessage value(Object value){
        this.value = value;
        return this;
    }
    private String id;
    private Object key;
    private Object value;

    public ByteBuf encode()throws Exception{
        ByteBuf bb = super.encode();
        String msg = new StringBuffer()
                .append(this.getType())
                .append(Constants.SPLIT)
                .append(id)
                .append(Constants.SPLIT)
                .append(key)
                .append(Constants.SPLIT)
                .append(value)
                .toString();
        bb.writeBytes(msg.getBytes(CharsetUtil.UTF_8));
        return bb;
    }

    public Message decode(ByteBuf bb) throws Exception{
        String msg = bb.toString(CharsetUtil.UTF_8);
        String[] msgs = msg.split("[|]");
        Message message = new PutMessage().id(msgs[1]).key(msgs[2]).value(msgs[3]);
        return message;
    }

    public String getId() {
        return id;
    }

    public Object getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
