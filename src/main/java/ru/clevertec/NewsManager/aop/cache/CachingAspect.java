package ru.clevertec.NewsManager.aop.cache;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**

 This class represents a caching aspect that provides caching functionality for methods annotated with @Cacheable.
 It uses a CacheFactory to create and manage different types of caches.
 */

@Slf4j
@Aspect
@Component
@Profile("test")
public class CachingAspect  {

    private final Map<String, CacheI<String, Object>> caches = new ConcurrentHashMap<>();

    @Autowired
    private CacheFactory cacheFactory;

    /**
     * Intercepts the execution of methods annotated with @Cacheable and applies caching logic.
     *
     * @param joinPoint  the join point for the intercepted method
     * @return the cached value or the result of the method invocation
     * @throws Throwable if an exception occurs during method execution
     */

    @Around("@annotation(cacheable)")
    public Object cache(ProceedingJoinPoint joinPoint) throws Throwable {
        CacheI<String, Object> cache = caches.computeIfAbsent("myCaches",
                k -> cacheFactory.createCache());
        switch (joinPoint.getSignature().getName()) {
            case "read" -> {
                String cacheKey = generateCacheKey(joinPoint);
                Object cachedValue = cache.get(cacheKey);
                if (cachedValue != null) {
                    return cachedValue;
                } else {
                    Object result = joinPoint.proceed();
                    cache.put(cacheKey, result);
                    return result;
                }
            }
            case "create" -> {
                Object resultCreate = joinPoint.proceed();
                cache.put(resultCreate.toString(), Arrays.stream(joinPoint.getArgs()).findFirst().get());
                return resultCreate;
            }
            case "delete" -> {
                String cacheKey1 = generateCacheKey(joinPoint);
                Object cachedValue1 = cache.get(cacheKey1);
                if (cachedValue1 != null) {
                    cache.remove(cacheKey1);
                    return joinPoint.proceed();
                } else {
                    return joinPoint.proceed();
                }
            }
            case "update" -> {
                for (Object args : joinPoint.getArgs()) {
                    Object cachedValue2 = cache.get(String.valueOf(args));
                    if (cachedValue2 != null) {
                        Object result = joinPoint.proceed();
                        cache.update(String.valueOf(args), Arrays.stream(joinPoint.getArgs()).skip(1).findFirst().get());
                        return result;
                    }
                }
                return joinPoint.proceed();
            }
        }
        return null;
    }

    /**
     * Generates a cache key based on the first argument of the intercepted method.
     * @param joinPoint the join point for the intercepted method
     * @return the generated cache key
     */

    private String generateCacheKey(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            return Arrays.stream(args).findFirst().get().toString();
        } else {
            throw new IllegalArgumentException("Invalid cache key: args is null or empty");
        }
    }
}
