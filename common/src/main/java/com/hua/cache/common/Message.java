package com.hua.cache.common;

import io.netty.buffer.ByteBuf;

public interface Message {
    public ByteBuf encode() throws Exception;
    public Message decode(ByteBuf bb) throws Exception;
}
