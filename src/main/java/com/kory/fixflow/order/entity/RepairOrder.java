package com.kory.fixflow.order.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 保修工单实体类
 */
@Data
public class RepairOrder {
    private Long id;        // 工单ID
    private String orderNo;   // 工单编号
    private String title;   // 工单标题
    private String description;     // 问题描述
    private String issueType;       // 问题类型
    private Integer urgency;        // 紧急程度
    private Integer status;         // 工单状态
    private Long applicantId;       // 提交人ID
    private Long departmentId;      // 提交人所属部门ID
    private Long assetId;           // 关联资产ID，可为空
    private Long assigneeId;        // 指派的维修人员ID
    private String rejectReason;    // 驳回原因
    private LocalDateTime assignTime;   // 派单时间
    private LocalDateTime acceptTime;   // 接单时间
    private LocalDateTime finishTime;   // 完成时间
    private LocalDateTime confirmTime;  // 用户确认时间
    private LocalDateTime submitTime;   // 提交时间
    private Integer isDeleted;          // 逻辑删除
    private LocalDateTime createTime;   // 创建时间
    private LocalDateTime updateTime;   // 更新时间
}
