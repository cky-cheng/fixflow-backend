package com.kory.fixflow.common.exception;

import com.kory.fixflow.common.enums.ResultCodeEnum;
import com.kory.fixflow.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler{
    
    /*
    处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    /*
    处理权限不足异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e) {
        return Result.fail(ResultCodeEnum.FORBIDDEN);
    }
    
    /*
    处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        
        // 获取第一个字段错误
        FieldError fieldError = e.getBindingResult().getFieldError();
        
        // 如果可以拿到具体字段提示，则返回     否则返回统一参数错误提示
        String message = fieldError != null
                ? fieldError.getDefaultMessage() : ResultCodeEnum.PARAM_ERROR.getMessage();
        
        return Result.fail(ResultCodeEnum.PARAM_ERROR.getCode(), message);
    }
    
    /*
    兜底异常处理
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.fail(ResultCodeEnum.SYSTEM_ERROR);
    }
}
