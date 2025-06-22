package com.health.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * 检查组数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckgroupDTO implements Serializable {

    private Integer id;

    /**
     * 检查组编码
     */
    private String code;

    /**
     * 检查组名称
     */
    @NotBlank(message = "检查组名称不能为空")
    private String name;

    /**
     * 助记码
     */
    private String helpCode;

    /**
     * 适用性别（0：不限，1：男，2：女）
     */
    @Pattern(regexp = "^[012]$", message = "性别只能是0、1、2")
    private String sex;

    /**
     * 项目说明
     */
    private String remark;

    /**
     * 注意事项
     */
    private String attention;

    /**
     * 关联的检查项ID列表
     */
    private List<Integer> checkitemIds;
}