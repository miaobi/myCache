package com.hua.cache.server.test;

import com.hua.cache.server.data.mapper.CategoryMapper;
import com.hua.cache.server.data.model.CatNode;
import com.hua.cache.server.data.model.Category;
import com.hua.cache.server.data.model.IndexMap;
import com.hua.cache.server.util.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BuildIndexTest {
    @Autowired
    private CategoryMapper categoryMapper;
    private static AtomicInteger counter =  new AtomicInteger(0);
    @Test
    public void build() throws Exception{
        FileUtil fu = new FileUtil("d:/cat.index");
        IndexMap<Integer, CatNode> indexMap = IndexMap.getInstance();
        List<Category> catList = categoryMapper.selectAllCat();
        Integer catId;
        Integer parentId;
        for(Category cat : catList){
            catId = cat.getCatId();
            parentId = cat.getParentId();
            if(parentId != 0){
                CatNode parentNode = indexMap.get(parentId);
                if(parentNode == null){
                    throw new Exception("not find parentNode");
                }
                parentNode.getChildList().add(catId);
            }
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            byte[] bytes = cat.getCatName().getBytes();
            buffer.put(bytes);
            CatNode catNode = new CatNode(cat);
            indexMap.put(catId, catNode);
        }
        List<Category> topCatList = categoryMapper.selectL0CateList();
        for (Category c: topCatList) {
            CatNode subNode = indexMap.get(c.getCatId());
            print(subNode,indexMap,fu);
        }
        fu.close();
    }

    private void print(CatNode subNode,IndexMap<Integer, CatNode> indexMap, FileUtil fu) throws IOException {
        subNode.setRecordId(counter.getAndIncrement());
        fu.write(subNode);
        System.out.println(subNode);
        List<Integer> list = subNode.getChildList();
        for (Integer i : list) {
            CatNode subNode2 = indexMap.get(i);
            print(subNode2,indexMap,fu);
        }
    }


    @Test
    public void readTest(){
        try {
            FileUtil<CatNode> fu = new FileUtil("D:/cat.index");
            fu.loadFile();
            while (fu.hasRemaining()){
                CatNode catNode = new CatNode();
                fu.read(catNode);
                System.out.println(catNode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
