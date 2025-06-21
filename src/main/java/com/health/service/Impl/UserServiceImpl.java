package com.health.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.health.domain.dto.LoginDTO;
import com.health.domain.vo.Result;
import com.health.domain.entity.User;
import com.health.mapper.UserMapper;
import com.health.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result login(LoginDTO loginDTO) {
        // 参数校验
        if (StrUtil.isBlank(loginDTO.getUsername())) {
            return Result.error("用户名不能为空");
        }
        if (StrUtil.isBlank(loginDTO.getPassword())) {
            return Result.error("密码不能为空");
        }
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        wrapper.eq(User::getPassword, loginDTO.getPassword());

        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            return Result.error("用户名或密码错误");
        }

        return Result.success(user);
    }
}
