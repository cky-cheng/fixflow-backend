package com.kory.fixflow.common.enums;

import lombok.Getter;

/**
 * 统一响应状态码枚举
 */
@Getter
public enum ResultCodeEnum {
    
    SUCCESS(200, "操作成功"),   // 通用
    PARAM_ERROR(400, "请求参数错误"), // 参数校验错误
    UNAUTHORIZED(401, "用户未登录"), // 未登录，登录认证时用
    FORBIDDEN(403, "权限不足"),    // 无权限，权限控制
    NOT_FOUND(404, "请求资源不存在"),    // 资源不存在
    BUSINESS(5001, "业务处理失败"),   // 通用业务异常
    SYSTEM_ERROR(500, "系统异常，请稍后再试");    // 系统内部异常
    
    
    private final Integer code;
    private final String message;
    
    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
}
