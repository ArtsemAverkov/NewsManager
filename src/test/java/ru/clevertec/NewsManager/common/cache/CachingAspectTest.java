package ru.clevertec.NewsManager.common.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.NewsManager.aop.cache.CacheFactory;
import ru.clevertec.NewsManager.aop.cache.CacheI;
import ru.clevertec.NewsManager.aop.cache.Cacheable;
import ru.clevertec.NewsManager.aop.cache.CachingAspect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

/**
 * Unit tests for the {@link CachingAspect} class.
 */
class CachingAspectTest {

    @Mock
    private CacheFactory cacheFactory;

    @InjectMocks
    private CachingAspect cachingAspect;

    /**
     * Sets up the test by initializing mock objects.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Tests the {@link CachingAspect#cache(ProceedingJoinPoint, Cacheable)} method for cache read operation.
     * @throws Throwable if an error occurs during the test
     */
    @Test
    void cacheRead() throws Throwable {

        Cacheable cacheable = mock(Cacheable.class);
        when(cacheable.value()).thenReturn("testCache");
        CacheI cache = mock(CacheI.class);
        when(cacheFactory.createCache()).thenReturn(cache);
        when(cache.get("key")).thenReturn("value");
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

        Signature signature = mock(Signature.class);
        when(signature.getName()).thenReturn("read");
        when(joinPoint.getSignature()).thenReturn(signature);

        when(joinPoint.getSignature().getName()).thenReturn("read");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"key"});

        Object result = cachingAspect.cache(joinPoint, cacheable);

        assertEquals("value", result);
        verify(cacheFactory, times(1)).createCache();
        verify(cache, times(1)).get("key");
        verify(joinPoint, never()).proceed();
    }

    /**
     * Tests the {@link CachingAspect#cache(ProceedingJoinPoint, Cacheable)} method for cache create operation.
     * @throws Throwable if an error occurs during the test
     */
    @Test
    void cacheCreate() throws Throwable {
         Cacheable cacheable = mock(Cacheable.class);
         when(cacheable.value()).thenReturn("testCache");
         CacheI cache = mock(CacheI.class);
         when(cacheFactory.createCache()).thenReturn(cache);
         ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
         Signature signature = mock(Signature.class);
         when(signature.getName()).thenReturn("create");
         when(joinPoint.getSignature()).thenReturn(signature);
         when(joinPoint.getSignature().getName()).thenReturn("create");
         when(joinPoint.getArgs()).thenReturn(new Object[]{"value"});
         when(joinPoint.proceed()).thenReturn("key");
         Object result = cachingAspect.cache(joinPoint, cacheable);
         assertEquals("key", result);
         verify(cacheFactory, times(1)).createCache();
         verify(cache, times(1)).put("key", "value");
         verify(joinPoint, times(1)).proceed();
     }

    /**
     * Tests the {@link CachingAspect#cache(ProceedingJoinPoint, Cacheable)} method for cache delete operation.
     * @throws Throwable if an error occurs during the test
     */
    @Test
    void cacheDelete() throws Throwable {
        Cacheable cacheable = mock(Cacheable.class);
        when(cacheable.value()).thenReturn("testCache");
        CacheI cache = mock(CacheI.class);
        when(cacheFactory.createCache()).thenReturn(cache);
        when(cache.get("key")).thenReturn("value");
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Signature signature = mock(Signature.class);
        when(signature.getName()).thenReturn("delete");
        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.getSignature().getName()).thenReturn("delete");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"key"});
        when(joinPoint.proceed()).thenReturn("value");
        Object result = cachingAspect.cache(joinPoint, cacheable);
        assertEquals("value", result);
        verify(cacheFactory, times(1)).createCache();
        verify(cache, times(1)).get("key");
        verify(cache, times(1)).remove("key");
        verify(joinPoint, times(1)).proceed();
    }

    /**
     * Tests the {@link CachingAspect#cache(ProceedingJoinPoint, Cacheable)} method for cache update operation.
     * @throws Throwable if an error occurs during the test
     */
    @Test
    void cacheUpdate() throws Throwable {
        Cacheable cacheable = mock(Cacheable.class);
        when(cacheable.value()).thenReturn("testCache");
        CacheI cache = mock(CacheI.class);
        when(cacheFactory.createCache()).thenReturn(cache);
        when(cache.get("key")).thenReturn("oldValue");
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Signature signature = mock(Signature.class);
        when(signature.getName()).thenReturn("update");
        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.getSignature().getName()).thenReturn("update");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"key", "newValue"});
        when(joinPoint.proceed()).thenReturn("newValue");
        Object result = cachingAspect.cache(joinPoint, cacheable);
        assertEquals("newValue", result);
        verify(cacheFactory, times(1)).createCache();
        verify(cache, times(1)).get("key");
        verify(cache, times(1)).update("key", "newValue");
        verify(joinPoint, times(1)).proceed();
    }
}

