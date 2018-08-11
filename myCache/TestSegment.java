package myCache;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReentrantLock;

public class TestSegment<K, V> extends ReentrantLock {
    static final int MAXIMUM_CAPACITY = 1 << 30;
    volatile int count;
    int initialCapacity;
    int threshold;
    long maxSegmentWeight;
    Queue<TestEntry<K, V>> accessQueue;
    final long expireAfterAccessNanos;
    TestEntry<K, V>[] table;

    TestSegment(int initialCapacity, long maxSegmentWeight, long expireAfterAccessNanos) {
        this.initialCapacity = initialCapacity;
        this.maxSegmentWeight = maxSegmentWeight;
        this.expireAfterAccessNanos = expireAfterAccessNanos;
        this.table = new TestEntry[initialCapacity];
        this.threshold = initialCapacity * 3 / 4; // 0.75
        this.accessQueue = new ConcurrentLinkedDeque<TestEntry<K, V>>();
    }

    boolean isExpired(TestEntry<K, V> entry, long now) {
        if (now - entry.getAccessTime() >= expireAfterAccessNanos) {
            return true;
        }
        return false;
    }

    TestEntry<K, V> copyEntry(TestEntry<K, V> original, TestEntry<K, V> newNext) {
        TestEntry<K, V> newEntry = new TestEntry(original.getKey(), original.getValue(), original.getHash(), newNext);
        return newEntry;
    }

    TestEntry<K, V> removeEntryFromChain(
            TestEntry<K, V> first, TestEntry<K, V> entry) {
        accessQueue.remove(entry);
        int newCount = count;
        TestEntry<K, V> newFirst = entry.getNext();
        for (TestEntry<K, V> e = first; e != entry; e = e.getNext()) {
            TestEntry<K, V> next = copyEntry(e, newFirst);
            if (next != null) {
                newFirst = next;
            } else {
                accessQueue.remove(e);
                newCount--;
            }
        }
        this.count = newCount;
        return newFirst;
    }

    boolean removeEntry(TestEntry<K, V> entry, int hash) {
        int newCount = this.count - 1;
        TestEntry<K, V>[] table = this.table;
        int index = hash & (table.length - 1);
        TestEntry<K, V> first = table[index];

        for (TestEntry<K, V> e = first; e != null; e = e.getNext()) {
            if (e == entry) {
                TestEntry<K, V> newFirst =
                        removeEntryFromChain(first, e);
                newCount = this.count - 1;
                table[index] = newFirst;
                this.count = newCount;
                return true;
            }
        }
        return false;
    }

    public void preCleanup(long now) {
        TestEntry<K, V> e;
        while ((e = accessQueue.peek()) != null && isExpired(e, now)) {
            if (!removeEntry(e, e.getHash())) {
                throw new AssertionError();
            }
        }
    }

    public void expand() {
        TestEntry<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity >= MAXIMUM_CAPACITY) {
            return;
        }
        int newCount = count;
        TestEntry<K, V>[] newTable = new TestEntry[oldCapacity << 1];
        threshold = newTable.length * 3 / 4;
        int newMask = newTable.length - 1;
        for (int oldIndex = 0; oldIndex < oldCapacity; ++oldIndex) {
            TestEntry<K, V> head = oldTable[oldIndex];
            if (head != null) {
                TestEntry<K, V> next = head.getNext();
                int headIndex = head.getHash() & newMask;
                if (next == null) {
                    newTable[headIndex] = head;
                } else {
                    TestEntry<K, V> tail = head;
                    int tailIndex = headIndex;
                    for (TestEntry<K, V> e = next; e != null; e = e.getNext()) {
                        int newIndex = e.getHash() & newMask;
                        if (newIndex != tailIndex) {
                            tailIndex = newIndex;
                            tail = e;
                        }
                    }
                    newTable[tailIndex] = tail;
                    for (TestEntry<K, V> e = head; e != tail; e = e.getNext()) {
                        int newIndex = e.getHash() & newMask;
                        TestEntry<K, V> newNext = newTable[newIndex];
                        TestEntry<K, V> newFirst = copyEntry(e, newNext);
                        if (newFirst != null) {
                            newTable[newIndex] = newFirst;
                        } else {
                            accessQueue.remove(e);
                            newCount--;
                        }
                    }
                }
            }
        }
        table = newTable;
        this.count = newCount;
    }

    public void put(K key, int hash, V value) {
        lock();
        try {
            long now = System.currentTimeMillis();
            preCleanup(now);

            int newCount = this.count + 1;
            if (newCount > this.threshold) { // ensure capacity
                expand();
                newCount = this.count + 1;
            }

            TestEntry<K, V>[] table = this.table;
            int index = hash & (table.length - 1);
            TestEntry<K, V> first = table[index];

            // Look for an existing entry.
            for (TestEntry<K, V> e = first; e != null; e = e.getNext()) {
                K entryKey = e.getKey();
                if (e.getHash() == hash
                        && entryKey != null
                        && key.equals(entryKey)) {
                    e.setValue(value);
                    e.setAccessTime(now);
                    accessQueue.remove(e);
                    accessQueue.add(e);
                    return;
                }
            }
            // Create a new entry.
            TestEntry<K, V> newEntry = new TestEntry(key, value, hash, first);
            table[index] = newEntry;
            newEntry.setAccessTime(now);
            accessQueue.add(newEntry);
            newCount = this.count + 1;
            this.count = newCount; // write-volatile
        } finally {
            unlock();
        }
    }

    TestEntry<K, V> getEntry(Object key, int hash) {
        TestEntry<K, V>[] table = this.table;
        int index = hash & (table.length - 1);
        for (TestEntry<K, V> e = table[index]; e != null; e = e.getNext()) {
            if (e.getHash() != hash) {
                continue;
            }
            K entryKey = e.getKey();
            if (entryKey == null) {
                continue;
            }
            if (key.equals(entryKey)) {
                return e;
            }
        }
        return null;
    }

    public V get(K key, int hash){
        if (count != 0) { //
            TestEntry<K, V> e = getEntry(key, hash);
            if (e != null) {
                long now = System.currentTimeMillis();
                if (!isExpired(e, now)) {
                    V value = e.getValue();
                    if(value != null) {
                        e.setAccessTime(now);
                        accessQueue.remove(e);
                        accessQueue.add(e);
                        return value;
                    }
                }
            }
        }
        return null;
    }
}
