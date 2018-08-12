package com.hua.cache.common.util;

import com.hua.cache.common.Message;
import com.hua.cache.common.MessageType;
import com.hua.cache.common.impl.*;
import io.netty.buffer.ByteBuf;

/**
 * 工厂模式
 * 封装对多种消息实例的生产
 */
public class MessageFactory {
    public static Message createMsgBy(ByteBuf bb) throws Exception {
        Message msg = null;
        HeadMessage head = new HeadMessage();
        head.decode(bb);
        int type = head.getType();
        int length = head.getLength();
        if(MessageType.PUT.value()==type){
            msg = new PutMessage()
                    .length(length);
        }else if(MessageType.GET.value()==type){
            msg = new GetMessage()
                    .length(length);
        }else if(MessageType.FILE.value()==type){
            msg = new FileWriteMessage()
                    .length(length);
        }else if(MessageType.FILE_REQ.value()==type){
            msg = new FileListMessage()
                    .length(length);
        }
        return msg;
    }
}
