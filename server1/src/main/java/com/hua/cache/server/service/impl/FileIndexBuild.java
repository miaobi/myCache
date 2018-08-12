package com.hua.cache.server.service.impl;

import com.hua.cache.server.data.mapper.CategoryMapper;
import com.hua.cache.server.data.model.CatNode;
import com.hua.cache.server.data.model.Category;
import com.hua.cache.server.data.model.IndexMap;
import com.hua.cache.server.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FileIndexBuild {
    private static IndexMap<Integer, CatNode> indexMap = IndexMap.getInstance();
    private static AtomicInteger counter =  new AtomicInteger(0);
    FileUtil fu = new FileUtil("D://workspace_intellij/serverCat.index");

    @Autowired
    private CategoryMapper categoryMapper;

    public void build() throws Exception{
        List<Category> catList = categoryMapper.selectAllCat();
        for(Category category : catList){
            CatNode catNode = new CatNode(category);
            indexMap.put(category.getCatId(), catNode);
        }
        Integer catId;
        Integer parentId;
        for(CatNode catNode : indexMap.values()){
            catId = catNode.getCatId();
            parentId = catNode.getParentId();
            if(parentId != 0){
                CatNode parentNode = indexMap.get(parentId);
                if(parentNode == null){
                    throw new Exception("not find parentNode");
                }
                parentNode.getChildList().add(catId);
            }
        }
        writeFile();
    }

    public void  writeFile(){
        List<Category> topCatList = categoryMapper.selectL0CateList();
        try {
            for (Category c: topCatList) {
                CatNode subNode = indexMap.get(c.getCatId());
                print(subNode);
            }
            fu.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void print(CatNode subNode) throws IOException {
        subNode.setRecordId(counter.getAndIncrement());
        fu.write(subNode);
        System.out.println(subNode);
        List<Integer> list = subNode.getChildList();
        for (Integer i : list) {
            CatNode subNode2 = indexMap.get(i);
            print(subNode2);
        }
    }
}
