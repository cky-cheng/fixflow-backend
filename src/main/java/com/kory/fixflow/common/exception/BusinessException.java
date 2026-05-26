package com.kory.fixflow.common.exception;

import com.kory.fixflow.common.enums.ResultCodeEnum;
import lombok.Getter;

/**
 * 自定义业务异常
 */
@Getter
public class BusinessException extends RuntimeException{
    private final Integer code;   // 业务状态码
    
    /*
    只传错误提示
     */
    public BusinessException(String message) {
        super(message);
        this.code = ResultCodeEnum.BUSINESS.getCode();
    }
    
    /*
    传入状态码枚举
     */
    public BusinessException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }
    
    /*
    自定义状态码和提示
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
