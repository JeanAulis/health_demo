package com.health.service;

import com.health.domain.dto.CheckgroupDTO;
import com.health.domain.query.PageQueryDTO;
import com.health.domain.vo.Result;

public interface CheckgroupService {
    
    Result findPage(PageQueryDTO pageQueryDTO);
    
    Result delete(Integer id);
    
    Result add(CheckgroupDTO checkgroupDTO);
    
    // 根据ID查询检查组信息
    Result findById(Integer id);
    
    // 根据检查组ID查询关联的检查项ID列表
    Result findCheckItemIdsByCheckGroupId(Integer id);
    
    // 编辑检查组（先删除后添加）
    Result edit(CheckgroupDTO checkgroupDTO, Integer[] checkitemIds);
}