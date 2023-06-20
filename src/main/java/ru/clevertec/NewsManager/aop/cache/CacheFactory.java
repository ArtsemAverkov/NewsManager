package ru.clevertec.NewsManager.aop.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**

 This class represents a CacheFactory that creates different types of caches based on the specified algorithm.
 The cache algorithm and maximum size are configurable through Spring properties.
 */

@Component
public class CacheFactory {
    private final int maxSize;
    private final String algorithm;

    /**
     * Constructs a CacheFactory with the specified maximum size and algorithm.
     *
     * @param maxSize   the maximum size of the cache
     * @param algorithm the cache algorithm to be used
     */

    @Autowired
    public CacheFactory(@Value("${cache.max-size}") int maxSize,
                        @Value("${cache.algorithm}") String algorithm) {
        this.maxSize = maxSize;
        this.algorithm = algorithm;
    }

    /**
     * Creates a cache based on the configured algorithm.
     * @param <K> the type of the cache key
     * @param <V> the type of the cache value
     * @return a cache instance based on the configured algorithm
     * @throws IllegalArgumentException if an invalid cache algorithm is configured
     */
    public <K, V> CacheI<K, V> createCache() {
        switch (algorithm.toUpperCase()) {
            case "LRU":
                return new LruCache<>(maxSize);
            case "LFU":
                return new LfuCache<>(maxSize);
            default:
                throw new IllegalArgumentException("Invalid cache algorithm: " + algorithm);
        }
    }
}





