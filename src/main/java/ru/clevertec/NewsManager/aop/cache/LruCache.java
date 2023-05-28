package ru.clevertec.NewsManager.aop.cache;

import java.util.*;


public class LruCache<K, V> implements CacheI<K,V> {
    private final int maxSize;
    private final Map<K, V> cache;
    private final Queue<K> lruQueue;

    public LruCache(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<K, V>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxSize;
            }
        };
        this.lruQueue = new LinkedList<K>();
    }


    @Override
    public void put(K key, V value) {
        if (cache.size() >= maxSize) {
            K lruKey = lruQueue.poll();
            cache.remove(lruKey);
        }
        cache.put(key, value);
        lruQueue.offer(key);
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        if (value != null) {
            lruQueue.remove(key);
            lruQueue.offer(key);
            return value;
        }
        return null;
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        lruQueue.remove(key);
    }

    @Override
    public void update(K key, V value) {
        cache.put(key, value);
        lruQueue.remove(key);
        lruQueue.offer(key);
    }
}
