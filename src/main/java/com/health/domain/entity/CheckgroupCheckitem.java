package com.health.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 检查组和检查项关联表实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_checkgroup_checkitem")
public class CheckgroupCheckitem implements Serializable {

    // 检查组ID
    private Integer checkgroupId;

    // 检查项ID
    private Integer checkitemId;
}