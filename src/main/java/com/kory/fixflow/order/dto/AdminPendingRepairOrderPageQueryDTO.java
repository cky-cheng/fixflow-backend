package com.kory.fixflow.order.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 管理员查询待受理工单分页 DTO
 */
@Data
public class AdminPendingRepairOrderPageQueryDTO {
    /*
    当前页码
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    /*
    每页条数
     */
    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer pageSize = 5;
    
    /*
    工单编号    支持模糊查询
     */
    private String orderNo;
    
    /*
    工单标题    支持模糊查询
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
    
    /*
    MySQL 分页偏移量
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
