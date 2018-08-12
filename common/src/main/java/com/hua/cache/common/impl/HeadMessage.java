package com.hua.cache.common.impl;

import com.hua.cache.common.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class HeadMessage implements Message{
    private int type;
    private int length;
    public Message length(int length){
        this.length = length;
        return this;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getLength() {
        return length;
    }

    @Override
    public ByteBuf encode() throws Exception {
        ByteBuf bb = PooledByteBufAllocator.DEFAULT.buffer();
        bb.writeInt(type);
        bb.writeInt(length);
        return bb;
    }

    @Override
    public Message decode(ByteBuf bb) throws Exception {
        this.type = bb.readInt();
        this.length = bb.readInt();
        return this;
    }
}
