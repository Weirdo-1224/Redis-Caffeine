package com.example.redis_caffeine;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RedisCaffeineApplicationTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testRedis() {
        redisTemplate.opsForValue().set("test", "Hello Redis");
        String value = (String) redisTemplate.opsForValue().get("test");
        System.out.println(value);  // 应输出 "Hello Redis"
    }

}
