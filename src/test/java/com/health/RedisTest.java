package com.health;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.concurrent.TimeUnit;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Redis测试类
 * 测试Redis的基本操作功能
 * 
 * @author jean Aulis
 */
@SpringBootTest
public class RedisTest {

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void redisSet(){
        stringRedisTemplate.opsForValue().set("gender", "male");
    }

    @Test
    public void redisGet(){
        String gender = stringRedisTemplate.opsForValue().get("gender");
        System.out.println(gender);
    }
}