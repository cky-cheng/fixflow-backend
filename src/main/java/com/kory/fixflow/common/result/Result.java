package com.kory.fixflow.common.result;

import com.kory.fixflow.common.enums.ResultCodeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 统一返回结果接口
 * @param <T>
 */
@Getter
@Setter
public class Result<T> {
    
    private Integer code;   // 业务状态码
    private String message; // 提示信息
    private T data;         // 真实返回数据
    
    /*
    成功  无数据
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), null);
    }
    
    /*
    成功  有数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), data);
    }
    
    /*
    失败  默认情况
     */
    public static <T> Result<T> fail() {
        return new Result<>(ResultCodeEnum.BUSINESS.getCode(), ResultCodeEnum.BUSINESS.getMessage(), null);
    }
    
    /*
    失败  自定义提示
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(ResultCodeEnum.BUSINESS.getCode(), message, null);
    }
    
    /*
    失败  按指定枚举
     */
    public static <T> Result<T> fail(ResultCodeEnum resultCodeEnum) {
        return new Result<>(resultCodeEnum.getCode(), resultCodeEnum.getMessage(), null);
    }
    
    /*
    失败  自定义状态码与提示
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }
    
    public Result() {
    }
    
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
}
