package com.hua.cache.server.service.api;

public interface Cache<K,V> {
    public boolean put(K k, V v);
    public V get(K k);
    public void start();
    public boolean shutdown();
    public boolean refresh(K k);
}
