package com.health.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_checkitem")
public class Checkitem implements Serializable {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 检查项编码
     */
    private String code;

    /**
     * 检查项名称
     */
    private String name;

    /**
     * 适用性别：0-不限，1-男，2-女
     */
    private String sex;

    /**
     * 适用年龄
     */
    private String age;

    /**
     * 价格
     */
    private Float price;

    /**
     * 检查项类型：1-检查，2-检验
     */
    private String type;

    /**
     * 注意事项
     */
    private String attention;

    /**
     * 项目说明
     */
    private String remark;
}
