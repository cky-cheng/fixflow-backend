package com.kory.fixflow.order.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 我的工单分页查询 DTO
 */
@Data
public class MyRepairOrderPageQueryDTO {
    
    /**
     * 当前页码
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer pageSize = 5;
    
    /**
     * 工单编号，支持模糊查询
     */
    private String orderNo;
    
    /**
     * 工单标题，支持模糊查询
     */
    private String title;
    
    /**
     * 问题类型
     */
    private String issueType;
    
    /**
     * 紧急程度
     */
    private Integer urgency;
    
    /**
     * 工单状态
     *  0待受理，1已驳回，2待接单，3处理中，4待确认，5已完成，已评价
     */
    private Integer status;
    
    /**
     * MySQL 分页偏移量
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
    
}
