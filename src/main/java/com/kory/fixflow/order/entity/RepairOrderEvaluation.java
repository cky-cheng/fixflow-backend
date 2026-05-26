package com.kory.fixflow.order.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工单评价实体表
 */
@Data
public class RepairOrderEvaluation {
    
    /*
    评价ID
     */
    private Long id;
    
    /*
    工单ID
     */
    private Long orderId;
    
    /*
    评分
     */
    private Integer score;
    
    /*
    评价内容
     */
    private String content;
    
    /*
    评价人ID
     */
    private Long evaluatorId;
    
    /*
    评价时间
     */
    private LocalDateTime evaluateTime;
    
    /*
    创建时间
     */
    private LocalDateTime createTime;
    
    /*
    更新时间
     */
    private LocalDateTime updateTime;
}
