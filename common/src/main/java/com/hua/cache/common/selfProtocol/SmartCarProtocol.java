package com.hua.cache.common.selfProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.util.Arrays;

/**
 * <pre>
 * 自定义协议
 *  数据包格式
 * +——----——+——-----——+——----——+
 * |协议开始标志|  长度             |   数据       |
 * +——----——+——-----——+——----——+
 * 1.协议开始标志head_data，为int类型的数据，16进制表示为0X76
 * 2.传输数据的长度contentLength，int类型
 * 3.要传输的数据
 * </pre>
 */
public class SmartCarProtocol {
    /**
     * 自定义协议消息开始标志
     */
    public static final int HEAD_DATA = 0X76;

    /**
     * 消息的开头的信息标志
     */
    private int head_data = HEAD_DATA;
    /**
     * 消息的类型
     */
    private int type;
    /**
     * 消息的长度
     */
    private int contentLength;
    /**
     * 消息的内容
     */
//    private byte[] content;
    private ByteBuf content;
    /**
     * 用于初始化，SmartCarProtocol
     *
     * @param contentLength
     *            协议里面，消息数据的长度
     * @param content
     *            协议里面，消息的数据
     */
    public SmartCarProtocol(int type, int contentLength, ByteBuf content) {
        this.type = type;
        this.contentLength = contentLength;
        this.content = content;
    }

    public int getHead_data() {
        return head_data;
    }

    public int getContentLength() {
        return contentLength;
    }

    public int getType() {
        return type;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ByteBuf getContent() {
        return content;
    }

    public void setContent(ByteBuf content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SmartCarProtocol [head_data=" + head_data + ", type="
                + type + ", contentLength="
                + contentLength + ", content=" + content.toString(CharsetUtil.UTF_8) + "]";
    }

}
