package com.health.service;

import com.health.domain.query.PageQueryDTO;
import com.health.domain.vo.Result;

public interface CheckgroupService {
    
    Result findPage(PageQueryDTO pageQueryDTO);
}