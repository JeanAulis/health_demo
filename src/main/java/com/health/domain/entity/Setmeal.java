package com.health.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("t_Setmeal")
public class Setmeal implements Serializable {

    private Integer id;

    private String name;

    private String code;

    @TableField("helpCode")
    private String helpCode;

    private String sex;

    private String age;

    private String price;

    private String remark;

    private String attention;

    private String img;
}
