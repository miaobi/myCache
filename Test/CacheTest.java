package Test;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CacheTest {

    public static String loadKey(String key){
        return "load " + key;
    }

    public static String getKey(String key){
        return "get " + key;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LoadingCache<String,String> cache = CacheBuilder.newBuilder()
                .maximumSize(210)
                .concurrencyLevel(6)
                //.expireAfterWrite(5, TimeUnit.SECONDS)
                .removalListener(e->{
                    System.out.println("removal --> key:"+e.getKey()+",value:"+e.getValue());
                })
                .recordStats()
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return loadKey(key);
                    }
                });

        String key2 = cache.get("key2",new Callable<String>(){
            @Override
            public String call() throws Exception {
                return getKey("key2");
            }
        });
        Thread.sleep(6000);
        cache.put("key1","value1");
        cache.put("key2","value1");
        cache.put("key3","value1");
        cache.put("key4","value1");
        System.out.println("key1:"+cache.get("key1"));
        System.out.println("key2:"+key2);
        System.out.println("key3:" + cache.getIfPresent("key3"));

    }
}
