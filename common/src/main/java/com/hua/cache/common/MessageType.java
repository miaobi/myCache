package com.hua.cache.common;

public enum MessageType{
    PUT(0),
    GET(1),
    FILE(2),
    FILE_REQ(3),
    FILE_LOAD(4),
    ;
    MessageType(int value){
        this.value = value;
    }
    private int value;
    public int value(){
        return this.value;
    }

}
