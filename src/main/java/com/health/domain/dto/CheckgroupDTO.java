package com.health.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckgroupDTO implements Serializable {

    private Integer id;

    private String code;

    @NotBlank(message = "检查组名称不能为空")
    private String name;

    private String helpCode;

    @Pattern(regexp = "^[012]$", message = "性别只能是0、1、2")
    private String sex;

    private String remark;

    private String attention;

    private List<Integer> checkitemIds;
}