package com.hua.cache.server.data.model;

import java.io.Serializable;
import java.util.concurrent.ConcurrentSkipListMap;

public class IndexMap<K,V> extends ConcurrentSkipListMap<K,V> implements Serializable {
    private static IndexMap indexMap;

    private IndexMap(){}

    public static IndexMap getInstance() {
        if(indexMap==null){
            synchronized (IndexMap.class){
                if(indexMap==null){
                    indexMap = new IndexMap();
                }
            }
        }
        return indexMap;
    }
}
