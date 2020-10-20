package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {
    ServerResponse<List<Category>> getChildParallelCategory(Integer categoryId);

    ServerResponse<String> addCategory(String categoryName, Integer parentId);

    ServerResponse<String> setCategoryName(String categoryName, Integer categoryId);

    ServerResponse<List<Integer>> getCategoryAndDeepChildrenCategory(Integer categoryId);

    /*    ServerResponse<List<Integer>> getDeepCategory(Integer categoryId);*/


}
