package com.health.web;

import com.health.domain.dto.LoginDTO;
import com.health.domain.entity.User;
import com.health.domain.vo.Result;
import com.health.service.RedisService;
import com.health.service.UserService;
import com.health.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

import java.util.UUID;

@Slf4j
@RestController
public class UserCotroller {
    
    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @PostMapping("/user/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        log.info("开始处理用户登录请求，参数：{}", loginDTO);

        try {
            // 调用业务层处理登录
            Result result = userService.login(loginDTO);
            
            // 登录成功时，生成UUID token并保存用户信息到Redis
            if (result.isFlag()) {
                User user = (User) result.getData();
                
                // 生成UUID作为token
                String token = UUID.randomUUID().toString().replace("-", "");
                
                // 将用户信息存储到Redis中
                redisService.setUserToken(token, user);
                
                log.info("用户登录成功，生成token，用户：{}", user.getUsername());
                
                // 返回token给前端
                return Result.success(token);
            }
            
            return result;
        } catch (Exception e) {
            log.error("用户登录处理异常，参数：{}", loginDTO, e);
            throw new RuntimeException("登录处理失败", e);
        }
    }

    @GetMapping("/user/getUsername")
    public Result getUsername(HttpServletRequest request) {
        log.info("获取当前登录用户名请求");
        
        try {
            // 从请求中提取token
            String token = TokenUtils.extractToken(request);
            log.info("获取到的token: {}", token);
            
            // 验证token是否有效
            if (!TokenUtils.isValidToken(token)) {
                log.warn("未找到有效token，请求头Authorization: {}, 请求参数token: {}", 
                        request.getHeader(TokenUtils.AUTHORIZATION_HEADER), 
                        request.getParameter(TokenUtils.TOKEN_PARAM));
                return Result.error("未登录或token已过期");
            }
            
            // 从Redis中获取用户信息
            User user = redisService.getUserByToken(token);
            
            if (user == null) {
                log.warn("用户信息已过期或不存在");
                return Result.error("登录已过期，请重新登录");
            }
            
            log.info("获取用户名成功：{}", user.getUsername());
            return Result.success(user.getUsername());
        } catch (Exception e) {
            log.error("获取用户名处理异常", e);
            throw new RuntimeException("获取用户信息失败", e);
        }
    }

    @GetMapping("/user/logout")
    public Result logout(HttpServletRequest request) {
        log.info("开始处理用户登出请求");

        try {
            // 从请求中提取token
            String token = TokenUtils.extractToken(request);
            log.info("获取到的token: {}", token);
            
            // 验证token是否有效
            if (!TokenUtils.isValidToken(token)) {
                log.warn("未提供有效的token");
                return Result.error("未提供有效的token");
            }
            
            // 从Redis中删除token
            boolean deleted = redisService.deleteUserToken(token);
            
            if (deleted) {
                log.info("用户登出成功，token已从Redis中删除");
            } else {
                log.warn("token在Redis中不存在或已过期");
            }
            
            // 无论删除是否成功都返回成功，避免暴露系统信息
            return Result.success("登出成功");
        } catch (Exception e) {
            log.error("用户登出处理异常", e);
            throw new RuntimeException("登出处理失败", e);
        }
    }

}
