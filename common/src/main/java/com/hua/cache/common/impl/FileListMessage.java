package com.hua.cache.common.impl;

import com.hua.cache.common.Message;
import com.hua.cache.common.MessageType;
import com.hua.cache.common.util.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.util.List;

public class FileListMessage extends HeadMessage implements Message {
    private String[] files;
    private int[] lengths;

    public int[] getLengths() {
        return lengths;
    }

    public String[] getFiles() {
        return files;
    }

    public FileListMessage lengths(int[] lengths){
        this.lengths = lengths;
        return this;
    }

    public FileListMessage files(String[] files){
        this.files = files;
        return this;
    }

    public FileListMessage(){
        super.setType(MessageType.FILE_REQ.value());
    }

    public ByteBuf encode() throws Exception {
        ByteBuf bb = super.encode();
        if(files!=null){
            StringBuffer sb = new StringBuffer();
            for (int i=0 ; i < files.length ; i++){
                sb.append(files[i]).append(Constants.SPLIT);
            }
            bb.writeBytes(sb.toString().getBytes(CharsetUtil.UTF_8));
        }
        return bb;
    }

    public Message decode(ByteBuf bb) throws Exception{
        if(lengths==null){
            return this;
        }
        String msg = bb.toString(CharsetUtil.UTF_8);
        if(msg==null){
            return this;
        }
        String[] fileNames = msg.split("[|]");
        if(fileNames.length==0){
            return this;
        }
        this.files = fileNames;
        return this;
    }
}
