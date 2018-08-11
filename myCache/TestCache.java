package myCache;


public class TestCache<K, V>{
    final int segmentMask;
    final int segmentShift;
    final TestSegment<K, V>[] segments;

    TestCache(int initialCapacity, int concurrencyLevel, long maxWeight) {
        int segmentShift = 0;
        int segmentCount = 1;
        while (segmentCount < concurrencyLevel && (segmentCount * 20 <= maxWeight)) {
            ++segmentShift;
            segmentCount <<= 1;
        }
        this.segmentShift = 32 - segmentShift;
        this.segmentMask = segmentCount - 1;
        this.segments = new TestSegment[segmentCount];

        int segmentCapacity = initialCapacity / segmentCount;
        if (segmentCapacity * segmentCount < initialCapacity) {
            ++segmentCapacity;
        }
        int segmentSize = 1;
        while (segmentSize < segmentCapacity) {
            segmentSize <<= 1;
        }

        long maxSegmentWeight = maxWeight / segmentCount + 1;
        long remainder = maxWeight % segmentCount;
        for (int i = 0; i < this.segments.length; ++i) {
            if (i == remainder) {
                maxSegmentWeight--;
            }
            this.segments[i] = new TestSegment<K, V>(segmentSize, maxSegmentWeight, 1000);
        }
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static int hash(Object o){
        int h = o.hashCode();
        h += (h << 15) ^ 0xffffcd7d;
        h ^= (h >>> 10);
        h += (h << 3);
        h ^= (h >>> 6);
        h += (h << 2) + (h << 14);
        return h ^ (h >>> 16);
    }

    TestSegment<K, V> segmentFor(int hash) {
        return segments[(hash >>> segmentShift) & segmentMask];
    }

    public void put(K key, V value) {
        checkNotNull(key);
        checkNotNull(value);
        int hash = hash(key);
        segmentFor(hash).put(key, hash, value);
    }

    public V get(K key) {
        int hash = hash(checkNotNull(key));
        return segmentFor(hash).get(key, hash);
    }
}
