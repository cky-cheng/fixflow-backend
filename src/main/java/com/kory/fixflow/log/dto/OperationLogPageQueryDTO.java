package com.kory.fixflow.log.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 操作日志分页查询 DTO
 */
@Data
public class OperationLogPageQueryDTO {
    
    /*
    当前页码
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码不能小于 1")
    private Integer pageNum = 1;

    /*
    每页条数
     */
    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数不能小于 1")
    @Max(value = 100, message = "每页条数不能超过 100")
    private Integer pageSize = 5;
    
    /*
    操作模块    模糊查询
     */
    private String module;
    
    /*
    操作内容    模糊查询
     */
    private String operation;
    
    /*
    操作人用户名  模糊查询
     */
    private String operatorUsername;
    
    /*
    操作状态
     */
    private Integer status;
    
    /*
    请求方式
     */
    private String requestMethod;
    
    /**
     * MySQL 分页偏移量
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
