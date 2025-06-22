package com.health.service;

import com.health.domain.entity.Checkitem;
import com.health.domain.query.PageQueryDTO;
import com.health.domain.vo.Result;

import java.util.List;

public interface CheckitemService {
    
    Result findPage(PageQueryDTO pageQueryDTO);

    Result add(Checkitem checkitem);
    
    Result findAll();
}
