package com.hua.cache.common.impl;

public enum FileType {
    INDEX(0),
    STORE(1),
    ;
    FileType(int value){
        this.value = value;
    }
    private int value;
    public int value(){
        return this.value;
    }

}
