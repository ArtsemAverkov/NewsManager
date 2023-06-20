package ru.clevertec.NewsManager.common.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import ru.clevertec.NewsManager.aop.cache.CacheFactory;
import ru.clevertec.NewsManager.aop.cache.CacheI;
import ru.clevertec.NewsManager.aop.cache.LruCache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link CacheFactory} class.
 */
public class CacheFactoryTest {

    @Autowired
    private CacheFactory cacheFactory;

    /**
     * Sets up the test by initializing the CacheFactory with default values.
     */
    @BeforeEach
    void setUp() {
        cacheFactory = new CacheFactory(10, "LRU");
    }

    /**
     * Tests the {@link CacheFactory#createCache()} method for creating an LRU cache.
     */
    @Test
    void createCacheLru() {

        CacheI<String, Object> cache = cacheFactory.createCache();

        assertTrue(cache instanceof LruCache);
        assertEquals(10, ReflectionTestUtils.getField(cache, "maxSize"));
    }
}

