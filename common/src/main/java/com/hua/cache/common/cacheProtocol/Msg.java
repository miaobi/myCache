package com.hua.cache.common.cacheProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class Msg {
    public int type;
    public int length;

    public Msg(int type, int length){
        this.type = type;
        this.length = length;
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

    public void setLength(int length) {
        this.length = length;
    }

    public ByteBuf encode(){
        ByteBuf out = PooledByteBufAllocator.DEFAULT.buffer();
        out.writeInt(this.type);
        out.writeInt(this.length);
        return out;
    }

}
