package com.health.service;

import com.health.domain.dto.LoginDTO;
import com.health.domain.vo.Result;

public interface UserService {
    Result login(LoginDTO loginDTO);
}
