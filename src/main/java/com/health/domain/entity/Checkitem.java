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


    @TableId(type = IdType.AUTO)
    private Integer id;

    private String code;

    private String name;

    private String sex;

    private String age;

    private Float price;

    private String type;

    private String attention;

    private String remark;
}
