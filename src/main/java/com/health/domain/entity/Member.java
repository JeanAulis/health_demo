package com.health.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_member")
public class Member implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("fileNumber")
    private String fileNumber;
    
    private String name;
    
    private String sex;

    @TableField("idCard")
    private String idCard;

    @TableField("phoneNumber")
    private String phoneNumber;

    @TableField("regTime")
    private LocalDate regTime;
    
    private String password;
    
    private String email;
    
    private LocalDate birthday;
    
    private String remark;
}