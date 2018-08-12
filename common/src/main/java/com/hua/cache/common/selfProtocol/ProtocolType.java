package com.hua.cache.common.selfProtocol;

public enum ProtocolType {
    PUT(0),
    GET(1),
    LODE_REQ(2),
    LODE_RSP(3),
    INDEX_REQ(4),
    INDEX_RSP(5),
    ;
    ProtocolType(int value){
        this.value = value;
    }
    private int value;
    public int value(){
        return this.value;
    }
}
