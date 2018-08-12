package com.hua.cache.server.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hua.cache.server.data.mapper.CategoryMapper;
import com.hua.cache.server.data.model.Category;
import com.hua.cache.server.data.model.TreeHelper;
import com.hua.cache.server.data.model.TreeNode;
import com.hua.cache.server.service.api.Cache;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class CatCacheImpl implements Cache<Integer,Object> {
    private LoadingCache<Integer,Object> cache;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public boolean put(Integer k, Object v) {
        if(cache!=null){
            cache.put(k,v);
            return true;
        }
        return false;
    }

    @Override
    public Object get(Integer k) {
        try {
            if(cache!=null){
                return cache.get(k);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void start() {
        List<Category> topNodeList = categoryMapper.selectL0CateList();
        for(Category topNode : topNodeList){
            TreeNode root = new TreeNode(topNode);
            Integer catId = topNode.getCatId();
            List<Category> subNodelist = categoryMapper.selectByTopId(topNode.getCatId());
            List<TreeNode> subTreeNodeList = TreeHelper.changeEnititiesToTreeNodes(subNodelist);
            TreeHelper treeHelper = new TreeHelper(root, subTreeNodeList);
            cache.put(catId,treeHelper);
        }
    }


    @Override
    public boolean shutdown() {
        if(cache!=null){
            cache.cleanUp();
            return true;
        }
        return false;
    }

    @Override
    public boolean refresh(Integer k) {
        if(cache!=null) {
            cache.refresh(k);
            return true;
        }
        return false;
    }
}
