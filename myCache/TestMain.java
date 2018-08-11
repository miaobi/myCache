package myCache;

public class TestMain {
    public static void main(String[] args) {
        TestCache cache = new TestCache(10, 2, 4);
        cache.put("key1", "value1");
        System.out.println(cache.get("key1"));
    }
}
