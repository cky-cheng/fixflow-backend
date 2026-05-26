package com.kory.fixflow.order.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工单详情 VO
 */
@Data
public class RepairOrderDetailVO {
    
    private Long id;        // 工单ID
    private String orderNo;     // 工单编号
    private String title;       // 标题
    private String description; // 问题描述
    private String issueType;   // 问题类型
    private Integer urgency;    // 紧急程度
    private Integer status;     // 工单状态
    private Long applicantId;   // 提交人ID
    private String applicantRealName;   // 提交人姓名
    private Long departmentId;  // 提交人部门ID
    private String deptName;    // 提交人部门名称
    private Long assetId;       // 关联资产ID
    private String assetNo;     // 资产编号
    private String assetName;   // 资产名称
    private Long assigneeId;    // 指派维修人员ID
    private String assigneeRealName;    // 指派维修人员姓名
    private String rejectReason;    // 驳回原因
    private LocalDateTime assignTime;   // 派单时间
    private LocalDateTime acceptTime;   // 接单时间
    private LocalDateTime finishTime;   // 维修完成时间
    private LocalDateTime confirmTime;  // 用户确认时间
    private LocalDateTime submitTime;   // 提交时间
    private LocalDateTime createTime;   // 创建时间
    private LocalDateTime updateTime;   // 更新时间
}
