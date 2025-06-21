package com.health.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_checkgroup")
public class Checkgroup implements Serializable {

    // 主键ID
    @TableId(type = IdType.AUTO)
    private Integer id;

    // 检查组编码
    private String code;

    // 检查组名称
    private String name;

    // 助记码
    @TableField("helpCode")
    private String helpCode;

    // 性别（0：不限，1：男，2：女）
    private String sex;

    // 项目说明
    private String remark;

    // 注意事项
    private String attention;

//    // 创建时间
//    @TableField(fill = FieldFill.INSERT)
//    private Date createTime;
//
//    // 更新时间
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    private Date updateTime;
}