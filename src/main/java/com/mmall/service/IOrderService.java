package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.Map;

public interface IOrderService {
    public ServerResponse pay(Long orderNo, Integer userId, String path);
    public ServerResponse aliCallback(Map<String, String> params);
    public ServerResponse<Boolean> queryOrderPayStatus(Integer userId,Long orderNo);
    }
