package com.hua.cache.server.util;

import com.hua.cache.server.data.model.CatNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileUtil<T> {
    private FileChannel fc;
    private ByteBuffer bb;
    private int length;
    private AtomicInteger position = new AtomicInteger(0);
    private static final Lock diskLock = new ReentrantLock();

    public FileUtil(String fileName){
        try {
            File f = new File(fileName);
            this.length = (int)f.length();
            this.fc = new RandomAccessFile(f,"rw").getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.bb = ByteBuffer.allocate(250 * 1024 * 1024);
    }

    public void write(Index index) throws IOException {
        if(index.getSize() + 7 > this.bb.remaining()) {
            // 写满了，flush
            flush2File();
        }
        index.write(bb);
    }

    public Index read(Index index) throws IOException {
        index.read(bb);
        position.getAndAdd(index.getSize());
        return index;
    }

    public void loadFile() throws IOException {
        this.fc.read(bb);
        bb.flip();
    }

    private void flush2File() {
        this.bb.flip();
        if(this.bb.hasRemaining()) {
            diskLock.lock();
            try {
                byte[] data = new byte[bb.limit()];
                bb.get(data,0,bb.limit());
                ByteBuffer temp = ByteBuffer.wrap(data);
                while(temp.hasRemaining()) {
                    this.fc.write(temp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                diskLock.unlock();
            }
        }
        this.bb.clear();
    }

    public void close() throws IOException {
        this.flush2File();
        fc.close();
    }

    public boolean hasRemaining(){
        if(position.get()>=this.length){
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        FileUtil<CatNode> fu = new FileUtil("d:/cat.index");
        CatNode catNode = new CatNode();
        catNode.setCatId(1);
        catNode.setParentId(2);
        catNode.setRecordId(3);
        List list = new ArrayList();
        list.add(5);
        list.add(6);
        catNode.setChildList(list);
        System.out.println(catNode);
        fu.write(catNode);
        fu.flush2File();
        CatNode catNode1 = new CatNode();
        fu.read(catNode1);
        System.out.println(catNode1);
        fu.close();
    }

}
