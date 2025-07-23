package com.example.redis_caffeine.annonation;

import com.example.redis_caffeine.utils.CacheType;

import java.lang.annotation.*;

/**
 * @author 周同学
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DoubleCache {
    String cacheName();
    String key(); //支持springEl表达式
    long l2TimeOut() default 120;
    CacheType type() default CacheType.FULL;
}