package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;


public interface IProductService {
    public ServerResponse productSave(Product product);

    public ServerResponse setSaleStatus(Integer productId,Integer status);

    public ServerResponse<ProductDetailVo> detail(Integer productId);
    public ServerResponse getProductList(int pageNum,int pageSize);

    public ServerResponse<PageInfo> search(String productName , Integer productId, int pageNum, int pageSize);

    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
    public ServerResponse<PageInfo> getProductByKeywordCategoryId(String keyword,Integer categoryId ,int pageNum,int pageSize,String orderBy);

    }
