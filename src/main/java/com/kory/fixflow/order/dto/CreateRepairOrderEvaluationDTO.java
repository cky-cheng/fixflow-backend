package com.kory.fixflow.order.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建工单评价 DTO
 */
@Data
public class CreateRepairOrderEvaluationDTO {
    /**
     * 评分
     */
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分不能低于 1 分")
    @Max(value = 5, message = "评分不能高于 5 分")
    private Integer score;
    
    /**
     * 评价内容
     */
    @Size(max = 500, message = "评价内容不能超过500个字符")
    private String content;
}
