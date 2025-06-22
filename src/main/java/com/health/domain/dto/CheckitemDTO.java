package com.health.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckitemDTO implements Serializable {

    private Integer id;

    @NotBlank(message = "检查项编码不能为空")
    private String code;

    @NotBlank(message = "检查项名称不能为空")
    private String name;

    @Pattern(regexp = "^[012]$", message = "性别只能是0、1、2")
    private String sex;

    private String age;

    @NotNull(message = "价格不能为空")
    private Float price;

    @Pattern(regexp = "^[12]$", message = "类型只能是1或2")
    private String type;

    private String attention;

    private String remark;
}