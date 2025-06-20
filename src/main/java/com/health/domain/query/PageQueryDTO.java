package com.health.domain.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页查询参数
 * @author ghy
 * @version 1.0.1
 * @since 2025-06-18 17:25
 */
@Data
@NoArgsConstructor  //无参构造器
@AllArgsConstructor  //全参构造器
@Builder   //构建者设计模式
public class PageQueryDTO {

    /**
     * 当前页码
     */
    private Integer currentPage = 1;

    /**
     * 每页显示记录数
     */
    private Integer pageSize = 10;

    /**
     * 查询条件
     */
    private String queryString;

}
