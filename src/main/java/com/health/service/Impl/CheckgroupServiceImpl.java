package com.health.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.domain.entity.Checkgroup;
import com.health.domain.query.PageQueryDTO;
import com.health.domain.vo.PageBean;
import com.health.domain.vo.Result;
import com.health.mapper.CheckgroupMapper;
import com.health.service.CheckgroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CheckgroupServiceImpl implements CheckgroupService {

    @Autowired
    private CheckgroupMapper checkgroupMapper;

    @Override
    public Result findPage(PageQueryDTO pageQueryDTO) {
        log.info("分页查询检查组，参数：{}", pageQueryDTO);

        try {
            // 创建分页对象
            Page<Checkgroup> page = new Page<>(pageQueryDTO.getCurrentPage(), pageQueryDTO.getPageSize());
            
            // 构建查询条件
            LambdaQueryWrapper<Checkgroup> wrapper = new LambdaQueryWrapper<>();
            // 根据名称模糊查询（如果查询条件不为空）
            wrapper.like(StrUtil.isNotBlank(pageQueryDTO.getName()), Checkgroup::getName, pageQueryDTO.getName());
            
            // 根据 queryString 进行查询（支持检查组编码、检查组名称或助记码）
            if (StrUtil.isNotBlank(pageQueryDTO.getQueryString())) {
                wrapper.and(w -> w.like(Checkgroup::getCode, pageQueryDTO.getQueryString())
                        .or().like(Checkgroup::getName, pageQueryDTO.getQueryString())
                        .or().like(Checkgroup::getHelpCode, pageQueryDTO.getQueryString()));
            }
            
            // 执行分页查询
            IPage<Checkgroup> pageResult = checkgroupMapper.selectPage(page, wrapper);
            
            // 构建返回结果
            PageBean<Checkgroup> pageBean = new PageBean<>(pageResult.getTotal(), pageResult.getRecords());
            
            log.info("分页查询检查组成功，总记录数：{}，当前页记录数：{}", pageResult.getTotal(), pageResult.getRecords().size());
            return Result.success("查询成功", pageBean);
            
        } catch (Exception e) {
            String errorMessage = Optional.ofNullable(e.getMessage()).orElse("未知错误");
            log.error("分页查询检查组失败: {}", errorMessage, e);
            return Result.error("查询失败：" + errorMessage);
        }
    }
}