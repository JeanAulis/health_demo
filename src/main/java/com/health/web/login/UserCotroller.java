package com.health.web.login;

import com.health.domain.dto.LoginDTO;
import com.health.domain.entity.User;
import com.health.domain.vo.Result;
import com.health.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
public class UserCotroller {
    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @PostMapping("/user/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        log.info("开始处理用户登录请求，参数：{}", loginDTO);

        // 调用业务层处理登录
        Result result = userService.login(loginDTO);
        
        // 登录成功时，保存用户信息到session对象中
        if (result.isFlag()) {
            User user = (User) result.getData();
            session.setAttribute("SESSION_USER", user);
            log.info("用户登录成功，已保存到session：{}", user.getUsername());
            // session.setMaxInactiveInterval(30 * 60); // 设置session存活时间为30分钟
        }
        
        return result;
    }

}
