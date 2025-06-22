package com.health.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.domain.entity.Checkitem;
import com.health.domain.entity.Setmeal;
import com.health.domain.query.PageQueryDTO;
import com.health.domain.vo.PageBean;
import com.health.domain.vo.Result;
import com.health.mapper.SetmealMapper;
import com.health.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;


    @Override
    public Result findPage(PageQueryDTO pageQueryDTO) {
        log.info("分页查询检查项，参数：{}", pageQueryDTO);

        try {
            // 创建分页对象
            Page<Setmeal> page = new Page<>(pageQueryDTO.getCurrentPage(), pageQueryDTO.getPageSize());

            // 构建查询条件
            LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
            // 根据名称模糊查询（如果查询条件不为空）
            wrapper.like(StrUtil.isNotBlank(pageQueryDTO.getName()), Setmeal::getName, pageQueryDTO.getName());

            // 根据 queryString 进行查询（支持项目编码或项目名称）
            if (StrUtil.isNotBlank(pageQueryDTO.getQueryString())) {
                wrapper.and(w -> w.like(Setmeal::getCode, pageQueryDTO.getQueryString())
                        .or()
                        .like(Setmeal::getName, pageQueryDTO.getQueryString()));
            }

            // 执行分页查询
            IPage<Setmeal> pageResult = setmealMapper.selectPage(page, wrapper);

            // 封装返回结果
            PageBean<Setmeal> resultPageBean = new PageBean<>();
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
