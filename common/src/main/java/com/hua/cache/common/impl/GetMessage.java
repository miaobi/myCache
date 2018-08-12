package com.hua.cache.common.impl;

import com.hua.cache.common.Message;
import com.hua.cache.common.MessageType;
import com.hua.cache.common.util.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class GetMessage extends HeadMessage implements Message {
    public GetMessage(){
        super.setType(MessageType.GET.value());
    }
    public GetMessage id(String id){
        this.id = id;
        return this;
    }
    public GetMessage key(Object key){
        this.key = key;
        return this;
    }
    private String id;
    private Object key;

    public ByteBuf encode()throws Exception{
        ByteBuf bb = super.encode();
        String msg = new StringBuffer()
                .append(this.getType())
                .append(Constants.SPLIT)
                .append(id)
                .append(Constants.SPLIT)
                .append(key)
                .toString();
        bb.writeBytes(msg.getBytes(CharsetUtil.UTF_8));
        return bb;
    }

    public Message decode(ByteBuf bb) throws Exception{
        String msg = bb.toString(CharsetUtil.UTF_8);
        String[] msgs = msg.split("[|]");
        Message message = new GetMessage().id(msgs[1]).key(msgs[2]);
        return message;
    }

    public String getId() {
        return id;
    }

    public Object getKey() {
        return key;
    }
}
