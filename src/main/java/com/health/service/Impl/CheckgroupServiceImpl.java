package com.health.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.domain.dto.CheckgroupDTO;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(CheckgroupDTO checkgroupDTO) {
        log.info("新增检查组，参数：{}", checkgroupDTO);

        try {
            // 参数校验
            if (checkgroupDTO == null) {
                log.warn("新增检查组失败：参数为空");
                return Result.error("参数不能为空");
            }

            // 检查检查组编码是否已存在（如果提供了编码）
            if (StrUtil.isNotBlank(checkgroupDTO.getCode())) {
                LambdaQueryWrapper<Checkgroup> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Checkgroup::getCode, checkgroupDTO.getCode());
                Checkgroup existingCheckgroup = checkgroupMapper.selectOne(wrapper);
                
                if (existingCheckgroup != null) {
                    log.warn("检查组编码已存在：{}", checkgroupDTO.getCode());
                    return Result.error("检查组编码已存在，请使用其他编码");
                }
            }

            // 创建检查组实体
            Checkgroup checkgroup = new Checkgroup();
            BeanUtils.copyProperties(checkgroupDTO, checkgroup);

            // 保存检查组基本信息
            int result = checkgroupMapper.insert(checkgroup);
            if (result <= 0) {
                log.error("新增检查组失败，数据库操作返回0");
                return Result.error("新增检查组失败");
            }

            // 处理检查组与检查项的关联关系
            List<Integer> checkitemIds = checkgroupDTO.getCheckitemIds();
            if (checkitemIds != null && !checkitemIds.isEmpty()) {
                for (Integer checkitemId : checkitemIds) {
                    if (checkitemId != null) {
                        CheckgroupCheckitem relation = CheckgroupCheckitem.builder()
                                .checkgroupId(checkgroup.getId())
                                .checkitemId(checkitemId)
                                .build();
                        
                        int relationResult = checkgroupCheckitemMapper.insert(relation);
                        if (relationResult <= 0) {
                            log.error("保存检查组与检查项关联关系失败，checkgroupId：{}，checkitemId：{}", 
                                    checkgroup.getId(), checkitemId);
                            throw new RuntimeException("保存检查组与检查项关联关系失败");
                        }
                    }
                }
                log.info("成功保存{}个检查组与检查项的关联关系", checkitemIds.size());
            }

            log.info("新增检查组成功，ID：{}", checkgroup.getId());
            return Result.success("新增检查组成功");

        } catch (Exception e) {
            String errorMessage = Optional.ofNullable(e.getMessage()).orElse("未知错误");
            log.error("新增检查组失败: {}", errorMessage, e);
            return Result.error("新增失败：" + errorMessage);
        }
    }

    @Override
    public Result findById(Integer id) {
        log.info("根据ID查询检查组，参数：{}", id);

        try {
            // 参数校验
            if (id == null || id <= 0) {
                log.warn("查询检查组失败：ID参数无效，id={}", id);
                return Result.error("ID参数无效");
            }

            // 查询检查组信息
            Checkgroup checkgroup = checkgroupMapper.selectById(id);
            if (checkgroup == null) {
                log.warn("查询检查组失败：检查组不存在，id={}", id);
                return Result.error("检查组不存在");
            }

            log.info("查询检查组成功，id={}, name={}", id, checkgroup.getName());
            return Result.success("查询成功", checkgroup);

        } catch (Exception e) {
            String errorMessage = Optional.ofNullable(e.getMessage()).orElse("未知错误");
            log.error("查询检查组失败: {}, id={}", errorMessage, id, e);
            return Result.error("查询失败：" + errorMessage);
        }
    }

    @Override
    public Result findCheckItemIdsByCheckGroupId(Integer id) {
        log.info("根据检查组ID查询关联的检查项ID列表，参数：{}", id);

        try {
            // 参数校验
            if (id == null || id <= 0) {
                log.warn("查询检查项ID列表失败：检查组ID参数无效，id={}", id);
                return Result.error("检查组ID参数无效");
            }

            // 查询检查组是否存在
            Checkgroup checkgroup = checkgroupMapper.selectById(id);
            if (checkgroup == null) {
                log.warn("查询检查项ID列表失败：检查组不存在，id={}", id);
                return Result.error("检查组不存在");
            }

            // 查询关联的检查项ID列表
            LambdaQueryWrapper<CheckgroupCheckitem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CheckgroupCheckitem::getCheckgroupId, id)
                   .select(CheckgroupCheckitem::getCheckitemId);
            
            List<CheckgroupCheckitem> relations = checkgroupCheckitemMapper.selectList(wrapper);
            
            // 提取检查项ID列表
            List<Integer> checkitemIds = relations.stream()
                    .map(CheckgroupCheckitem::getCheckitemId)
                    .collect(java.util.stream.Collectors.toList());

            log.info("查询检查项ID列表成功，检查组id={}, 关联检查项数量={}", id, checkitemIds.size());
            return Result.success("查询成功", checkitemIds);

        } catch (Exception e) {
            String errorMessage = Optional.ofNullable(e.getMessage()).orElse("未知错误");
            log.error("查询检查项ID列表失败: {}, 检查组id={}", errorMessage, id, e);
            return Result.error("查询失败：" + errorMessage);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result edit(CheckgroupDTO checkgroupDTO, Integer[] checkitemIds) {
        log.info("编辑检查组，参数：{}, 检查项IDs：{}", checkgroupDTO, checkitemIds);

        try {
            // 参数校验
            if (checkgroupDTO == null || checkgroupDTO.getId() == null) {
                log.warn("编辑检查组失败：参数无效");
                return Result.error("参数无效");
            }

            // 检查检查组是否存在
            Checkgroup existingCheckgroup = checkgroupMapper.selectById(checkgroupDTO.getId());
            if (existingCheckgroup == null) {
                log.warn("编辑检查组失败：检查组不存在，id={}", checkgroupDTO.getId());
                return Result.error("检查组不存在");
            }

            // 检查编码是否与其他检查组重复（排除自己）
            if (StrUtil.isNotBlank(checkgroupDTO.getCode())) {
                LambdaQueryWrapper<Checkgroup> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Checkgroup::getCode, checkgroupDTO.getCode())
                       .ne(Checkgroup::getId, checkgroupDTO.getId());
                Checkgroup duplicateCheckgroup = checkgroupMapper.selectOne(wrapper);
                
                if (duplicateCheckgroup != null) {
                    log.warn("检查组编码已存在：{}", checkgroupDTO.getCode());
                    return Result.error("检查组编码已存在，请使用其他编码");
                }
            }

            // 1. 先删除原有的检查组与检查项的关联关系
            QueryWrapper<CheckgroupCheckitem> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("checkgroup_id", checkgroupDTO.getId());
            checkgroupCheckitemMapper.delete(deleteWrapper);
            log.info("删除检查组原有关联关系，检查组ID：{}", checkgroupDTO.getId());

            // 2. 更新检查组基本信息
            Checkgroup checkgroup = new Checkgroup();
            checkgroup.setId(checkgroupDTO.getId());
            checkgroup.setCode(checkgroupDTO.getCode());
            checkgroup.setName(checkgroupDTO.getName());
            checkgroup.setHelpCode(checkgroupDTO.getHelpCode());
            checkgroup.setSex(checkgroupDTO.getSex());
            checkgroup.setRemark(checkgroupDTO.getRemark());
            checkgroup.setAttention(checkgroupDTO.getAttention());
            
            int updateResult = checkgroupMapper.updateById(checkgroup);
            if (updateResult <= 0) {
                log.warn("更新检查组信息失败，检查组ID：{}", checkgroupDTO.getId());
                return Result.error("更新检查组信息失败");
            }
            log.info("更新检查组基本信息成功，检查组ID：{}", checkgroupDTO.getId());

            // 3. 添加新的检查组与检查项的关联关系
            if (checkitemIds != null && checkitemIds.length > 0) {
                for (Integer checkitemId : checkitemIds) {
                    if (checkitemId != null && checkitemId > 0) {
                        CheckgroupCheckitem relation = new CheckgroupCheckitem();
                        relation.setCheckgroupId(checkgroupDTO.getId());
                        relation.setCheckitemId(checkitemId);
                        checkgroupCheckitemMapper.insert(relation);
                    }
                }
                log.info("添加检查组新关联关系成功，检查组ID：{}，关联检查项数量：{}", 
                        checkgroupDTO.getId(), checkitemIds.length);
            }

            log.info("编辑检查组成功，检查组ID：{}", checkgroupDTO.getId());
            return Result.success("编辑检查组成功");
            
        } catch (Exception e) {
            log.error("编辑检查组失败", e);
            return Result.error("编辑检查组失败：" + e.getMessage());
        }
    }
}