package myCache;

public class TestEntry<K, V> {
    final K key;
    final int hash;
    final TestEntry<K, V> next;
    volatile V value;
    volatile long accessTime;

    TestEntry(K key, V value, int hash, TestEntry<K, V> next) {
        this.key = key;
        this.value = value;
        this.hash = hash;
        this.next = next;
    }

    public K getKey() {
        return this.key;
    }
    public V getValue(){return this.value;}
    public int getHash() {
        return this.hash;
    }
    public TestEntry<K, V> getNext(){ return this.next;}
    public long getAccessTime() {
        return accessTime;
    }

    public void setValue(V value) {
        this.value = value;
    }
    public void setAccessTime(long time) {
        this.accessTime = time;
    }

}
