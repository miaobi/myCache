package com.hua.cache.server.test;

import com.hua.cache.server.data.mapper.CategoryMapper;
import com.hua.cache.server.data.model.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServerApplicationTests {
	@Autowired
	private CategoryMapper categoryMapper;
	@Test
	public void contextLoads() {
		List<Category> list = categoryMapper.selectL0CateList();
		print(list);
	}

	private void print(List<Category> list){
		if(list==null){
			return;
		}
		for (Category c: list) {
			System.out.println(c);
			Integer parentId = c.getCatId();
			List<Category> sublist = categoryMapper.selectByParentId(parentId);
			print(sublist);
		}
	}

}
