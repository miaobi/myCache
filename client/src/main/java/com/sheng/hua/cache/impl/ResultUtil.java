package com.sheng.hua.cache.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ResultUtil<T>{
//    private final ThreadLocal<T> THREAD_LOCAL = new ThreadLocal<T>();
    private final Map<String,String> resultMap = new ConcurrentHashMap<String,String>();
    private static ResultUtil resultUtil;

    public static ResultUtil getInstance() {
        if(resultUtil==null){
            synchronized (ResultUtil.class){
                if(resultUtil==null){
                    resultUtil = new ResultUtil();
                }
            }
        }
        return resultUtil;
    }

    public boolean putValue(String key,String value){
        resultMap.put(key,value);
        return true;
    }

    public String getResult(String key){
        return resultMap.remove(key);
    }


}
