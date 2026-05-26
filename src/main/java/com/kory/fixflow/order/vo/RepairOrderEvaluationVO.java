package com.kory.fixflow.order.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工单评价 VO
 */
@Data
public class RepairOrderEvaluationVO {
    
    /*
    评价 ID
     */
    private Long id;
    
    /*
    工单 ID
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
    评价人 ID
     */
    private Long evaluatorId;
    
    /*
    评价人姓名
     */
    private String evaluatorRealName;
    
    /*
    评价时间
     */
    private LocalDateTime evaluateTime;
}
