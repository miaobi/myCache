package com.hua.cache.common.cacheProtocol;

public enum MsgType {
    LODE_REQ(0),
    LODE_RSP(1),
    INDEX_REQ(3),
    INDEX_RSP(4),
    ;
    MsgType(int value){
        this.value = value;
    }
    private int value;
    public int value(){
        return this.value;
    }
}
