package com.health.domain.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String name;

}
