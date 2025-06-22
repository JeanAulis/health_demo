package com.health.service;

import com.health.domain.query.PageQueryDTO;
import com.health.domain.vo.Result;
import org.springframework.stereotype.Service;

@Service
public interface SetmealService {
    Result findPage(PageQueryDTO pageQueryDTO);
}
