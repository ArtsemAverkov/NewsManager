package ru.clevertec.NewsManager.aop.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

@Component
public interface CacheI <K,V>  {
    void put(K key, V value);
    V get(K key);
    void remove(K key);
    void update(K key, V value);
}
