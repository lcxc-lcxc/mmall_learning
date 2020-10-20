package com.mmall.service.Impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;



    @Override
    public ServerResponse<List<Category>> getChildParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);

    }

    @Override
    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        if(parentId==null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }



        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);

        int resultCount =categoryMapper.insertSelective(category);
        if (resultCount>0){
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    @Override
    public ServerResponse<String> setCategoryName(String categoryName, Integer categoryId) {
        if(categoryId==null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("修改品类名字参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount>0){
            return ServerResponse.createBySuccessMessage("更新品类名字成功");
        }


        return ServerResponse.createByErrorMessage("更新品类名字失败");
    }

    /**
     * 递归查询本节点的id及孩子节点的id
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse<List<Integer>> getCategoryAndDeepChildrenCategory(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId!=null){
            for (Category categoryItem : categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }

        return ServerResponse.createBySuccess(categoryIdList);
    }

    private Set<Category> findChildCategory(Set<Category> categorySet,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category!=null){
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem:categoryList){
            findChildCategory(categorySet,categoryItem.getId());
        }

        return categorySet;
    }

/*
    @Override
    public ServerResponse<List<Integer>> getDeepCategory(Integer categoryId) {

        List<Integer> resultList = new ArrayList<>();
        resultList.add(categoryId);
        resultList.addAll(recursiveCategory(categoryId));
        return ServerResponse.createBySuccess(resultList);
    }

    private List<Integer> recursiveCategory(Integer categoryId){

        List<Integer> idList = categoryMapper.selectIdByParentId(categoryId);
        if (CollectionUtils.isEmpty(idList)){
            return idList;
        }
        List<Integer> temp = new ArrayList<>();
        for(Integer i : idList){
            temp.addAll(recursiveCategory(i));
        }
        temp.addAll(idList);
        return temp;

    }
*/






}
