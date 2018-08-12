package com.hua.cache.server.test;

import com.hua.cache.server.data.model.CatNode;
import com.hua.cache.server.data.model.IndexMap;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MmapTest {

    public static void main(String[] args) throws Exception {
        long begin = System.currentTimeMillis();

        RandomAccessFile in = new RandomAccessFile("D:/workspace_intellij/serverCat.index", "r");
        FileChannel channel = in.getChannel();
        MappedByteBuffer buff = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

        IndexMap<Integer, CatNode> indexMap = IndexMap.getInstance();
        CatNode cat = null;
        while (buff.hasRemaining()) {
            cat = new CatNode();
            cat.setCatId(buff.getInt());
            cat.setParentId(buff.getInt());
            int recordId = buff.getInt();
            cat.setRecordId(recordId);
            int size = buff.getInt();
            int[] child = new int[size];
            for (int i = 0; i < size; i++) {
                child[i] = buff.getInt();
            }
            cat.setChild(child);
            indexMap.put(recordId, cat);
        }
        long end = System.currentTimeMillis();
        System.out.println("time is:" + (end - begin));


    }



}
