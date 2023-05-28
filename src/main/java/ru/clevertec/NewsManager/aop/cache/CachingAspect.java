package ru.clevertec.NewsManager.aop.cache;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CachingAspect  {

    private final Map<String, CacheI<String, Object>> caches = new ConcurrentHashMap<>();

    private final CacheFactory cacheFactory;


    @Around("@annotation(cacheable)")
    public Object cache(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        CacheI<String, Object> cache = caches.computeIfAbsent(cacheable.value(),
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
                        cache.update(String.valueOf(args), Arrays.stream(joinPoint.getArgs()).findFirst().get());
                        return result;
                    }
                }
                return joinPoint.proceed();
            }
        }
        return null;
    }

    private String generateCacheKey(ProceedingJoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs()).findFirst().get().toString();
    }
}
