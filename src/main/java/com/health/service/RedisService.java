package com.health.service;

import com.health.constants.RedisConstants;
import com.health.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class RedisService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    public void setUserToken(String token, User user, long timeout, TimeUnit unit) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("token不能为空");
        }
        if (user == null) {
            throw new IllegalArgumentException("用户信息不能为空");
        }
        
        String key = RedisConstants.USER_TOKEN_PREFIX + token;
        redisTemplate.opsForValue().set(key, user, timeout, unit);
        log.info("用户token已保存到Redis，key: {}, 用户: {}, 过期时间: {}{}",
                key, user.getUsername(), timeout, unit.toString().toLowerCase());
    }
    

    public void setUserToken(String token, User user) {
        setUserToken(token, user, RedisConstants.TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);
    }

    public User getUserByToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            log.warn("token为空，无法获取用户信息");
            return null;
        }
        
        String key = RedisConstants.USER_TOKEN_PREFIX + token;
        Object userObj = redisTemplate.opsForValue().get(key);
        
        if (userObj instanceof User) {
            User user = (User) userObj;
            log.debug("从Redis获取用户信息成功，key: {}, 用户: {}", key, user.getUsername());
            return user;
        } else {
            log.warn("Redis中未找到用户信息或数据类型不匹配，key: {}", key);
            return null;
        }
    }
    
    public boolean deleteUserToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            log.warn("token为空，无法删除");
            return false;
        }
        
        String key = RedisConstants.USER_TOKEN_PREFIX + token;
        Boolean deleted = redisTemplate.delete(key);
        boolean result = Boolean.TRUE.equals(deleted);
        
        log.info("删除用户token，key: {}, 删除结果: {}", key, result);
        return result;
    }

    public boolean hasUserToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        
        String key = RedisConstants.USER_TOKEN_PREFIX + token;
        Boolean exists = redisTemplate.hasKey(key);
        return Boolean.TRUE.equals(exists);
    }
    
    public boolean extendTokenExpire(String token, long timeout, TimeUnit unit) {
        if (token == null || token.trim().isEmpty()) {
            log.warn("token为空，无法延长过期时间");
            return false;
        }
        
        String key = RedisConstants.USER_TOKEN_PREFIX + token;
        Boolean result = redisTemplate.expire(key, timeout, unit);
        boolean success = Boolean.TRUE.equals(result);
        
        log.info("延长token过期时间，key: {}, 新过期时间: {}{}, 设置结果: {}",
                key, timeout, unit.toString().toLowerCase(), success);
        return success;
    }
}