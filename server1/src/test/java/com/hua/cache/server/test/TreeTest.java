package com.hua.cache.server.test;

import com.hua.cache.server.data.mapper.CategoryMapper;
import com.hua.cache.server.data.model.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TreeTest {
    private static final Map<Integer,Map> cache = new ConcurrentHashMap();
    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    public void contextLoads() {
        List<Category> list = categoryMapper.selectL0CateList();
        print(list);
        System.out.println("--------------------------------");
        for (Map.Entry e:cache.entrySet()) {
            ConcurrentSkipListMap m =(ConcurrentSkipListMap)e.getValue();

            for (Object e1: m.entrySet()) {
                System.out.println(e1);
            }
        }
    }

    private void print(List<Category> list){
        if(list==null){
            return;
        }
        for (Category c: list) {
            if(c.getTopId()<0){
                Map skipList = new ConcurrentSkipListMap();
                skipList.put(c.getCatId(),c);
                cache.put(c.getCatId(),skipList);
            }else {
                Map skipList = cache.get(c.getTopId());
                skipList.put(c.getCatId(),c);
            }
            System.out.println(c);
            Integer parentId = c.getCatId();
            List<Category> sublist = categoryMapper.selectByParentId(parentId);
            print(sublist);
        }
    }
}
