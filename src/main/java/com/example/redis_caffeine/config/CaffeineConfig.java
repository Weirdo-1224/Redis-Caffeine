package com.example.redis_caffeine.config;


import com.example.redis_caffeine.entity.Order;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.concurrent.TimeUnit;

/**
 * @author 周同学
 */
@Configuration
@EnableCaching
public class CaffeineConfig {

    //-----------------------------V1------V3-----------------------------------
    @Bean
    public Cache<String, Object> orderCache() {
        return Caffeine.newBuilder()
                .initialCapacity(128)
                .maximumSize(1024)
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build();
    }

    //-----------------------------V2------------------------------------------
    /*@Bean
    public CacheManager cacheManager(){
        CaffeineCacheManager cacheManager=new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(128)
                .maximumSize(1024)
                .expireAfterWrite(60, TimeUnit.SECONDS));
        return cacheManager;
    }*/
}
