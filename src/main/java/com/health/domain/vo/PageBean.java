package com.health.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果
 * @author ghy
 * @version 1.0.1
 * @since 2025-06-18 17:25
 */
@Data
@NoArgsConstructor  //无参构造器
@AllArgsConstructor  //全参构造器
public class PageBean<T> {

    /**
     * 总条数
     */
    private Long total;

    /**
     * 当前页集合
     */
    private List<T> rows;

}
