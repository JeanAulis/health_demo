package com.health.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回值，由前后端协定
 * @author ghy
 * @version 1.0.1
 * @since 2025-06-18 17:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result<T> {
    private boolean flag;
    private String message;
    private T data;

    public static <T> Result<T> success(){
        return new Result<>(true, "成功", null);
    }

    public static <T> Result<T> success(String message, T data){
        return new Result<>(true, message, data);
    }

    public static <T> Result<T> success(T data){
        return new Result<>(true, "成功", data);
    }

    public static <T> Result<T> error(){
        return new Result<>(false, "失败", null);
    }

    public static <T> Result<T> error(String message){
        return new Result<>(false, message, null);
    }
}
