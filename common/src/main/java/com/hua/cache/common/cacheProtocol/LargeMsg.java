package com.hua.cache.common.cacheProtocol;

public class LargeMsg extends Msg {
    //已读数据长度
    private int readLength;
    //数据内容
    private byte[] content;

    public LargeMsg(int type, int length, int readLength, byte[] content ) {
        super(type, length);
        this.readLength = readLength;
        this.content = content;
    }

    public int getReadLength() {
        return readLength;
    }

    public void setReadLength(int readLength) {
        this.readLength = readLength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
