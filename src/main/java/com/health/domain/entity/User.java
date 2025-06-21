package com.health.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("t_user")
public class User implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private LocalDate birthday;
    
    private String gender;
    
    private String username;
    
    private String password;
    
    private String remark;
    
    private String station;
    
    private String telephone;
}
