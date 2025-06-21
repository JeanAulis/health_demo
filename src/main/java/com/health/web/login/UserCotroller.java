package com.health.web.login;

import com.health.domain.dto.LoginDTO;
import com.health.domain.vo.Result;
import com.health.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserCotroller {
    @Autowired
    private UserService userService;

    @PostMapping("/user/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        log.info("开始处理用户登录请求，参数：{}", loginDTO);

        Result result = userService.login(loginDTO);
        return result;
    }

}
