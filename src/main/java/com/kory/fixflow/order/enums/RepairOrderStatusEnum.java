package com.kory.fixflow.order.enums;

import lombok.Getter;

/**
 * 工单状态枚举
 */
@Getter
public enum RepairOrderStatusEnum {
    // 待受理
    WAIT_ACCEPT(0, "待受理"),
    
    // 已驳回
    REJECTED(1, "已驳回"),
    
    // 待接单
    WAIT_RECEIVE(2, "待接单"),
    
    // 处理中
    PROCESSING(3, "处理中"),
    
    // 待确认
    WAIT_CONFIRM(4, "待确认"),
    
    // 已完成
    COMPLETED(5, "已完成"),
    
    // 已评价
    EVALUATED(6, "已评价");
    
    
    private final Integer code;
    private final String message;
    
    
    RepairOrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
