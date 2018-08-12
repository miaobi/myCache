package com.hua.cache.common.impl;

import com.hua.cache.common.Message;
import com.hua.cache.common.MessageType;

public class FileLoadMessage extends HeadMessage implements Message {
    public FileLoadMessage(){
        super.setType(MessageType.FILE_LOAD.value());
    }

}
