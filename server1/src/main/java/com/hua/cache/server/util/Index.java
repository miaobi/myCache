package com.hua.cache.server.util;

import java.nio.ByteBuffer;

public interface Index {
    public void write(ByteBuffer bb);
    public Index read(ByteBuffer bb);
    public int getSize();
}
