package com.hua.cache.common.cacheProtocol;

public class ShortMsg extends Msg{
    public int type;
    public int length;
    private byte[] content;

    public ShortMsg(int type, int length, byte[] content) {
        super(type, length);
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
