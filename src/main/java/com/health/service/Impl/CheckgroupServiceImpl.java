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
import com.health.mapper.CheckgroupCheckitemMapper;
import com.health.domain.entity.CheckgroupCheckitem;
import com.health.service.CheckgroupService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CheckgroupServiceImpl implements CheckgroupService {

    @Autowired
    private CheckgroupMapper checkgroupMapper;
    
    @Autowired
    private CheckgroupCheckitemMapper checkgroupCheckitemMapper;

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

    @Override
    public Result delete(Integer id) {
        log.info("删除检查组，参数：{}", id);

        try {
            // 参数校验
            if (id == null || id <= 0) {
                log.warn("删除检查组失败：ID参数无效，id={}", id);
                return Result.error("ID参数无效");
            }

            // 检查检查组是否存在
            Checkgroup checkgroup = checkgroupMapper.selectById(id);
            if (checkgroup == null) {
                log.warn("删除检查组失败：检查组不存在，id={}", id);
                return Result.error("检查组不存在");
            }

            // 检查是否有关联的检查项
            QueryWrapper<CheckgroupCheckitem> relationWrapper = new QueryWrapper<>();
            relationWrapper.eq("checkgroup_id", id);
            Integer relationCount = checkgroupCheckitemMapper.selectCount(relationWrapper);
            
            if (relationCount > 0) {
                log.warn("删除检查组失败：检查组下还有关联的检查项，id={}, 关联检查项数量={}", id, relationCount);
                return Result.error("删除失败：该检查组下还有关联的检查项，请先解除关联关系");
            }
            
            // TODO: 这里还可以添加其他业务逻辑检查，比如检查是否有关联的套餐等
            // 如果有关联数据，应该阻止删除或者提示用户

            // 执行删除操作
            int result = checkgroupMapper.deleteById(id);
            
            if (result > 0) {
                log.info("删除检查组成功，id={}, name={}", id, checkgroup.getName());
                return Result.success("删除成功");
            } else {
                log.warn("删除检查组失败：数据库操作失败，id={}", id);
                return Result.error("删除失败");
            }
            
        } catch (Exception e) {
            String errorMessage = Optional.ofNullable(e.getMessage()).orElse("未知错误");
            log.error("删除检查组失败: {}, id={}", errorMessage, id, e);
            return Result.error("删除失败：" + errorMessage);
        }
    }
}