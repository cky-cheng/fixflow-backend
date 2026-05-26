package com.kory.fixflow.order.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 我的待确认工单分页查询 DTO
 */
@Data
public class MyWaitConfirmPageQueryDTO {

    /*
    当前页码
     */
    @NotNull(message = "当前页码不能为空")
    @Min(value = 1, message = "当前页码不能小于 1")
    private Integer pageNum = 1;

    /*
    每页条数
     */
    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数不能小于 1")
    @Max(value = 100, message = "每页条数不能超过 100")
    private Integer pageSize = 5;
    
    /*
    工单编号    模糊搜索
     */
    private String orderNo;
    
    /*
    工单标题    模糊搜索
     */
    private String title;
    
    /*
    问题类型
     */
    private String issueType;
    
    /*
    紧急程度
     */
    private Integer urgency;
    
    /**
     * MySQL 分页偏移量
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
