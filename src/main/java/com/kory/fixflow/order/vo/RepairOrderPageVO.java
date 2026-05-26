package com.kory.fixflow.order.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工单分页展示 VO
 */
@Data
public class RepairOrderPageVO {
    
    private Long id;    // 工单ID
    private String orderNo;     // 工单编号
    private String title;       // 工单标题
    private String issueType;   // 问题类型
    private Integer urgency;    // 紧急程度
    private Integer status;     // 工单状态
    private Long applicantId;   // 提交人ID
    private String applicantRealName;   // 提交人姓名
    private Long departmentId;  // 所属部门ID
    private String deptName;    // 部门名称
    private Long assetId;       // 关联资产ID
    private String assetNo;     // 资产编号
    private String assetName;   // 资产名称
    private LocalDateTime submitTime;   // 提交时间
}
