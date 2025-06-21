package com.health.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.domain.entity.Checkitem;
import com.health.domain.query.PageQueryDTO;
import com.health.domain.vo.PageBean;
import com.health.domain.vo.Result;
import com.health.mapper.CheckitemMapper;
import com.health.service.CheckitemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CheckitemServiceImpl implements CheckitemService {

    @Autowired
    private CheckitemMapper checkitemMapper;

    @Override
    public Result findPage(PageQueryDTO pageQueryDTO) {
        log.info("分页查询检查项，参数：{}", pageQueryDTO);

        try {
            // 创建分页对象
            Page<Checkitem> page = new Page<>(pageQueryDTO.getCurrentPage(), pageQueryDTO.getPageSize());
            
            // 构建查询条件
            LambdaQueryWrapper<Checkitem> wrapper = new LambdaQueryWrapper<>();
            // 根据名称模糊查询（如果查询条件不为空）
            wrapper.like(StrUtil.isNotBlank(pageQueryDTO.getName()), Checkitem::getName, pageQueryDTO.getName());
            // 也可以根据 queryString 进行查询
            wrapper.like(StrUtil.isNotBlank(pageQueryDTO.getQueryString()), Checkitem::getName, pageQueryDTO.getQueryString());
            
            // 执行分页查询
            IPage<Checkitem> pageResult = checkitemMapper.selectPage(page, wrapper);
            
            // 封装返回结果
            PageBean<Checkitem> resultPageBean = new PageBean<>();
            resultPageBean.setTotal(pageResult.getTotal());
            resultPageBean.setRows(pageResult.getRecords());

            log.info("分页查询检查项成功，共查询到[{}]条记录", pageResult.getTotal());
            return Result.success(resultPageBean);

        } catch (Exception e) {
            String errorMessage = Optional.ofNullable(e.getMessage()).orElse("未知错误");
            log.error("分页查询检查项失败: {}", errorMessage, e);
            return Result.error("查询失败：" + errorMessage);
        }
    }
}
