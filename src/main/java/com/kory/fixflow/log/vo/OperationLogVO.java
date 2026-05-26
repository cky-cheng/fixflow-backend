package com.kory.fixflow.log.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志展示VO
 */
@Data
public class OperationLogVO {
    
    /*
    日志ID
     */
    private Long id;
    
    /*
    操作模块
     */
    private String module;
    
    /*
    操作内容
     */
    private String operation;
    
    /*
    请求方式
     */
    private String requestMethod;
    
    /*
    请求路径
     */
    private String requestUri;
    
    /*
    JAVA 方法名
     */
    private String javaMethod;
    
    /*
    请求参数 JSON 字符串
     */
    private String requestParams;
    
    /*
    操作人ID
     */
    private Long operatorId;
    
    /*
    操作人用户名
     */
    private String operatorUsername;
    
    /*
    请求IP
     */
    private String ip;
    
    /*
    操作状态
     */
    private Integer status;
    
    /*
    错误信息
     */
    private String errorMessage;
    
    /*
    耗时，毫秒
     */
    private Long costTime;
    
    /*
    创建时间
     */
    private LocalDateTime createTime;
}
