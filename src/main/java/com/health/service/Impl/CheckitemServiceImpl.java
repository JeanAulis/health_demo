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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
            
            // 根据 queryString 进行查询（支持项目编码或项目名称）
            if (StrUtil.isNotBlank(pageQueryDTO.getQueryString())) {
                wrapper.and(w -> w.like(Checkitem::getCode, pageQueryDTO.getQueryString())
                                 .or()
                                 .like(Checkitem::getName, pageQueryDTO.getQueryString()));
            }
            
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

    @Override
    public Result add(Checkitem checkitem) {
        log.info("新增检查项，参数：{}", checkitem);

        try {
            // 检查项目编码是否已存在
            LambdaQueryWrapper<Checkitem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Checkitem::getCode, checkitem.getCode());
            Checkitem existingItem = checkitemMapper.selectOne(wrapper);

            if (existingItem != null) {
                log.warn("检查项编码已存在：{}", checkitem.getCode());
                return Result.error("检查项编码已存在，请使用其他编码");
            }

            // 执行新增操作
            int result = checkitemMapper.insert(checkitem);

            if (result > 0) {
                log.info("新增检查项成功，ID：{}", checkitem.getId());
                return Result.success("新增检查项成功");
            } else {
                log.error("新增检查项失败，数据库操作返回0");
                return Result.error("新增检查项失败");
            }

        } catch (Exception e) {
            String errorMessage = Optional.ofNullable(e.getMessage()).orElse("未知错误");
            log.error("新增检查项失败: {}", errorMessage, e);
            return Result.error("新增失败：" + errorMessage);
        }
    }

    @Override
    public Result findAll() {
        log.info("查询所有检查项");

        try {
            // 构建查询条件，按照创建时间排序
            LambdaQueryWrapper<Checkitem> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByAsc(Checkitem::getId); // 按ID升序排列，确保结果稳定
            
            // 执行查询
            List<Checkitem> checkitems = checkitemMapper.selectList(wrapper);
            
            log.info("查询所有检查项成功，共查询到[{}]条记录", checkitems.size());
            return Result.success(checkitems);

        } catch (Exception e) {
            String errorMessage = Optional.ofNullable(e.getMessage()).orElse("未知错误");
            log.error("查询所有检查项失败: {}", errorMessage, e);
            return Result.error("查询失败：" + errorMessage);
        }
    }
}
