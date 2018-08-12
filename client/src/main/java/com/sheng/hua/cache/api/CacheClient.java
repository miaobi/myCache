package com.sheng.hua.cache.api;

public interface CacheClient<K,V> {
    public boolean put(K k, V v) throws Exception;
    public V get(K k) throws Exception;

}
