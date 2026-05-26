package com.kory.fixflow.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 驳回工单 DTO
 */
@Data
public class RejectRepairOrderDTO {
    
    /*
    驳回原因
     */
    @NotBlank(message = "驳回原因不能为空")
    @Size(max = 500, message = "驳回原因不能超过500个字符")
    private String rejectReason;
    
    
}
