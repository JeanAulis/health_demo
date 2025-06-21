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

    /**
     * 主键ID（新增时不需要，修改时必需）
     */
    private Integer id;

    /**
     * 检查项编码
     */
    @NotBlank(message = "检查项编码不能为空")
    private String code;

    /**
     * 检查项名称
     */
    @NotBlank(message = "检查项名称不能为空")
    private String name;

    /**
     * 适用性别：0-不限，1-男，2-女
     */
    @Pattern(regexp = "^[012]$", message = "性别只能是0、1、2")
    private String sex;

    /**
     * 适用年龄
     */
    private String age;

    /**
     * 价格
     */
    @NotNull(message = "价格不能为空")
    private Float price;

    /**
     * 检查项类型：1-检查，2-检验
     */
    @Pattern(regexp = "^[12]$", message = "类型只能是1或2")
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