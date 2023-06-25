package ru.clevertec.NewsManager.utill;

public interface CacheConstants {
    String CACHE_CONDITION = "${spring.cache.type} == 'lruCache' or ${spring.cache.type} == 'lfuCache' or ${spring.cache.type} == 'redis'";
}
