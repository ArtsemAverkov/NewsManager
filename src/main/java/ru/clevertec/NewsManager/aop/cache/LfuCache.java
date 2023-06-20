package ru.clevertec.NewsManager.aop.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 This class represents a Least Frequently Used (LFU) cache implementation.
 It stores key-value pairs and evicts the least frequently used item when the cache is full.
 @param <K> the type of the cache key
 @param <V> the type of the cache value
 */

@Slf4j
public class LfuCache<K, V> implements CacheI<K,V>{
    private  int maxSize;
    private  Map<K, V> cache;
    private  Map<K, Integer> frequencies;
    private  Queue<K> lfuQueue;

    /**
     Constructs an LFU cache with the specified maximum size.
     @param maxSize the maximum size of the cache
     */

    public LfuCache(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<>();
        this.frequencies = new HashMap<>();
        this.lfuQueue = new PriorityQueue<>(Comparator.comparingInt(frequencies::get));
    }

    @Override
    public void put(K key, V value) {
        if (cache.size() >= maxSize) {
            K lfuKey = lfuQueue.poll();
            cache.remove(lfuKey);
            frequencies.remove(lfuKey);
        }
        cache.put(key, value);
        frequencies.compute(key, (k, v) -> v == null ? 1 : v + 1);
        lfuQueue.offer(key);
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        if (value != null) {
           frequencies.compute(key, (k, v) -> v + 1);
           lfuQueue.remove(key);
           lfuQueue.offer(key);
            return value;
        }
        return null;
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        frequencies.remove(key);
        lfuQueue.remove(key);
    }

    @Override
    public void update(K key, V value) {
        cache.put(key, value);
        frequencies.compute(key, (k, v) -> v = 1);
        lfuQueue.offer(key);
    }
}

