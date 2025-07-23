package com.example.redis_caffeine.aspect;

import com.example.redis_caffeine.annonation.DoubleCache;
import com.example.redis_caffeine.utils.CacheConstant;
import com.example.redis_caffeine.utils.CacheType;
import com.example.redis_caffeine.utils.SpelExpressionUtils;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * @author 周同学
 */
@Slf4j
@Component
@Aspect
@AllArgsConstructor
public class CacheAspect {

    private final Cache<String, Object> cache;
    private final RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(com.example.redis_caffeine.annonation.DoubleCache)")
    public void cacheAspect() {}

    @Around("cacheAspect()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();

            // 解析参数
            String[] paramNames = signature.getParameterNames();
            Object[] args = point.getArgs();
            TreeMap<String, Object> treeMap = new TreeMap<>();
            for (int i = 0; i < paramNames.length; i++) {
                treeMap.put(paramNames[i], args[i]);
            }

            DoubleCache annotation = method.getAnnotation(DoubleCache.class);
            String elResult = SpelExpressionUtils.parse(annotation.key(), treeMap);
            String realKey = annotation.cacheName() + CacheConstant.ORDER + elResult;

            // 强制更新
            if (annotation.type() == CacheType.PUT) {
                Object object = point.proceed();
                redisTemplate.opsForValue().set(realKey, object, annotation.l2TimeOut(), TimeUnit.SECONDS);
                cache.put(realKey, object);
                return object;
            }

            // 删除
            if (annotation.type() == CacheType.DELETE) {
                redisTemplate.delete(realKey);
                cache.invalidate(realKey);
                return point.proceed();
            }

            // 优先查询 Caffeine
            Object caffeineCache = cache.getIfPresent(realKey);
            if (caffeineCache != null) {
                log.info("get data from caffeine");
                return caffeineCache;
            }

            // 其次查询 Redis
            Object redisCache = redisTemplate.opsForValue().get(realKey);
            if (redisCache != null) {
                log.info("get data from redis");
                cache.put(realKey, redisCache);
                return redisCache;
            }

            // 最后查询数据库
            log.info("get data from database");
            Object object = point.proceed();
            if (object != null) {
                redisTemplate.opsForValue().set(realKey, object, annotation.l2TimeOut(), TimeUnit.SECONDS);
                cache.put(realKey, object);
            }
            return object;
        } catch (Exception e) {
            log.error("Cache aspect error", e);
            throw e;
        }
    }
}