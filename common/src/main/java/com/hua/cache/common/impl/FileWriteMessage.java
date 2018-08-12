package com.hua.cache.common.impl;

import com.hua.cache.common.Message;
import com.hua.cache.common.MessageType;
import io.netty.buffer.ByteBuf;

public class FileWriteMessage extends HeadMessage implements Message {
    private int fileName;
    private int version;
    private int fileLength;

    public FileWriteMessage(){
        super.setType(MessageType.FILE.value());
    }

    public ByteBuf encode() throws Exception {
        return super.encode();
    }

    public Message decode(ByteBuf bb) throws Exception {
        return this;
    }

    public int getFileName() {
        return fileName;
    }

    public int getVersion() {
        return version;
    }

    public int getFileLength() {
        return fileLength;
    }

    public FileWriteMessage fileLength(int fileLength) {
        this.fileLength = fileLength;
        return this;
    }
    public FileWriteMessage fileName(int fileName) {
        this.fileName = fileName;
        return this;
    }
    public FileWriteMessage version(int version) {
        this.version = version;
        return this;
    }
}
