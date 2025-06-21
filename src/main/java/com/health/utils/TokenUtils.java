package com.health.utils;

import com.health.constants.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;


//Token工具类
//统一处理token的获取、验证等操作

@Slf4j
public class TokenUtils {

    public static final String BEARER_PREFIX = "Bearer ";
    

    public static final String AUTHORIZATION_HEADER = "Authorization";
    

    public static final String TOKEN_PARAM = "token";

    public static String extractToken(HttpServletRequest request) {
        if (request == null) {
            log.warn("请求对象为空，无法提取token");
            return null;
        }
        
        // 优先从请求头中获取token
        String token = request.getHeader(AUTHORIZATION_HEADER);
        
        // 如果请求头中没有token，尝试从请求参数中获取
        if (!StringUtils.hasText(token)) {
            token = request.getParameter(TOKEN_PARAM);
        }
        
        // 如果token以"Bearer "开头，去掉前缀
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            token = token.substring(BEARER_PREFIX.length());
        }
        
        log.debug("从请求中提取到的token: {}", token);
        return token;
    }
    

    public static boolean isValidToken(String token) {
        return StringUtils.hasText(token);
    }

    public static String generateRedisKey(String token) {
        if (!isValidToken(token)) {
            throw new IllegalArgumentException("token不能为空");
        }
        return RedisConstants.USER_TOKEN_PREFIX + token;
    }
}