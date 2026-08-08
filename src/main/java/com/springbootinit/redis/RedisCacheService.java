package com.springbootinit.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service

public class RedisCacheService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void setKey(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public String getKey(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
}
