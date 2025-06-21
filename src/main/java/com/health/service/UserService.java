package com.health.service;

import com.health.domain.dto.LoginDTO;
import com.health.domain.vo.PageBean;
import com.health.domain.vo.Result;

public interface UserService {
    // 登录
    Result login(LoginDTO loginDTO);
}
