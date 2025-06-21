package com.health.web;

import com.health.domain.dto.LoginDTO;
import com.health.domain.entity.User;
import com.health.domain.vo.Result;
import com.health.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class UserCotroller {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/user/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        log.info("开始处理用户登录请求，参数：{}", loginDTO);

        // 调用业务层处理登录
        Result result = userService.login(loginDTO);
        
        // 登录成功时，生成UUID token并保存用户信息到Redis
        if (result.isFlag()) {
            User user = (User) result.getData();
            
            // 生成UUID作为token
            String token = UUID.randomUUID().toString().replace("-", "");
            
            // 将用户信息存储到Redis中，设置过期时间为2小时
            String redisKey = "user:token:" + token;
            redisTemplate.opsForValue().set(redisKey, user, 2, TimeUnit.HOURS);
            
            log.info("用户登录成功，生成token为：{}，用户：{}", token, user.getUsername());
            
            // 返回token给前端
            return Result.success(token);
        }
        
        return result;
    }

}
