package com.health.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.health.service.CodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CodeServiceImpl implements CodeService {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    // 验证码在Redis中的key前缀
    private static final String CODE_PREFIX = "validate_code:";
    
    // 验证码有效期（分钟）
    private static final long CODE_EXPIRE_MINUTES = 5;
    
    @Override
    public String generateAndSendCode(String telephone) {
        log.info("开始生成验证码，手机号：{}", telephone);
        
        // 参数校验
        if (StrUtil.isBlank(telephone)) {
            log.error("手机号不能为空");
            return null;
        }
        
        try {
            // 生成6位随机数字验证码
            String code = generateRandomCode();
            
            // 打印验证码到控制台（模拟发送短信）
            log.info("=== 验证码发送 ===");
            log.info("手机号：{}", telephone);
            log.info("验证码：{}", code);
            log.info("有效期：{}分钟", CODE_EXPIRE_MINUTES);
            log.info("==================");
            
            // 存储验证码到Redis并设置过期时间
            String redisKey = CODE_PREFIX + telephone;
            redisTemplate.opsForValue().set(redisKey, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            
            log.info("验证码已存储到Redis，key：{}，有效期：{}分钟", redisKey, CODE_EXPIRE_MINUTES);
            
            return code;
            
        } catch (Exception e) {
            log.error("生成验证码失败，手机号：{}，异常信息：{}", telephone, e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    public boolean validateCode(String telephone, String inputCode) {
        try {
            // 从Redis中获取验证码
            String redisKey = CODE_PREFIX + telephone;
            String cachedCode = redisTemplate.opsForValue().get(redisKey);
            
            if (cachedCode == null) {
                log.warn("验证码不存在或已过期，手机号：{}", telephone);
                return false;
            }
            
            // 验证码校验
            boolean isValid = cachedCode.equals(inputCode);
            
            if (isValid) {
                // 验证成功，从Redis中删除验证码
                redisTemplate.delete(redisKey);
                log.info("验证码校验成功，手机号：{}，已从Redis删除", telephone);
            } else {
                log.warn("验证码校验失败，手机号：{}，输入验证码：{}，正确验证码：{}", telephone, inputCode, cachedCode);
            }
            
            return isValid;
            
        } catch (Exception e) {
            log.error("验证码校验异常，手机号：{}，异常信息：{}", telephone, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 生成6位随机数字验证码
     * @return 验证码
     */
    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        
        return code.toString();
    }
}
