package com.hua.cache.server.data.mapper;

import com.hua.cache.server.data.model.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    //查询所有的顶级类目
    List<Category> selectL0CateList();
    //查询某类目的子类目
    List<Category> selectByParentId(Integer parentId);
    //查询某个顶级类目下的所有类目
    List<Category> selectByTopId(Integer topId);
    //查询所有类目
    List<Category> selectAllCat();
}