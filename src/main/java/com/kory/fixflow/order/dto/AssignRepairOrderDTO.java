package com.kory.fixflow.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 派发工单 DTO
 */
@Data
public class AssignRepairOrderDTO {
    /**
     * 被指派维修人员ID
     */
    @NotNull(message = "维修人员不能为空")
    private Long assigneeId;
}

