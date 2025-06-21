package com.health.web;

import com.health.domain.dto.LoginDTO;
import com.health.domain.entity.User;
import com.health.domain.vo.Result;
import com.health.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

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

    @GetMapping("/user/getUsername")
    public Result getUsername(HttpServletRequest request) {
        log.info("获取当前登录用户名请求");
        
        // 从请求头中获取token，支持多种格式
        String token = request.getHeader("Authorization");
        
        // 如果请求头中没有token，尝试从请求参数中获取
        if (token == null || token.isEmpty()) {
            token = request.getParameter("token");
        }
        
        // 如果token以"Bearer "开头，去掉前缀
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        log.info("获取到的token: {}", token);
        
        if (token == null || token.isEmpty()) {
            log.warn("未找到token，请求头Authorization: {}, 请求参数token: {}", 
                    request.getHeader("Authorization"), request.getParameter("token"));
            return Result.error("未登录或token已过期");
        }
        
        // 从Redis中获取用户信息
        String redisKey = "user:token:" + token;
        log.info("查询Redis key: {}", redisKey);
        
        User user = (User) redisTemplate.opsForValue().get(redisKey);
        
        if (user == null) {
            log.warn("Redis中未找到用户信息，key: {}", redisKey);
            return Result.error("登录已过期，请重新登录");
        }
        
        log.info("获取用户名成功：{}", user.getUsername());
        return Result.success(user.getUsername());
    }

}
