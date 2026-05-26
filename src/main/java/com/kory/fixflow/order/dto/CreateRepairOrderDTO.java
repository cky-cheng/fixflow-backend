package com.kory.fixflow.order.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 提交报修工单 DTO
 */
@Data
public class CreateRepairOrderDTO {
    
    /**
     * 工单标题
     */
    @NotBlank(message = "工单标题不能为空")
    @Size(max = 200, message = "工单标题不能超过200个字符")
    private String title;
    
    
    /**
     * 问题描述
     */
    @NotBlank(message = "问题描述不能为空")
    private String description;
    
    
    /**
     * 问题类型
     */
    @NotBlank(message = "问题类型不能为空")
    @Size(max = 50, message = "问题类型不能超过50个字符")
    private String issueType;
    
    
    /**
     * 紧急程度
     */
    @NotNull(message = "紧急程度不能为空")
    @Min(value = 1, message = "紧急程度值不合法")
    @Max(value = 3, message = "紧急程度值不合法")
    private Integer urgency;
    
    
    /**
     * 关联资产ID，可为空
     */
    private Long assetId;
}
