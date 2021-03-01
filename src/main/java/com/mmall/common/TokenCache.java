package com.mmall.common;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TokenCache {

    //slf4j是很多log工具的外观组件,提供了统一的接口给log4j logback等
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX = "token_";

    private static LoadingCache<String,String> localCache
            = CacheBuilder
            .newBuilder()
            .initialCapacity(1000)
            .maximumSize(10000)
            .expireAfterWrite(12, TimeUnit.HOURS)//被放进后12个小时后回收
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现,当调用get取值时,如果key没有对应的值,就调用这个方法进行加载
                @Override
                public String load(String key) throws Exception {
                    return null;
                }
            });

    public static void setKey(String key,String value){
        localCache.put(key, value);
    }

    public static String getKey(String key) {
        String value = null;
        try {
            value = localCache.get(key);
            return value;
        }catch (Exception e){
            logger.error("LocalCache get error",e);
        }
        return null;
    }
}
