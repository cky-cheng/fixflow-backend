package com.kory.fixflow.order.mapper;

import com.kory.fixflow.order.entity.RepairOrderEvaluation;
import com.kory.fixflow.order.vo.RepairOrderEvaluationVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工单评价数据访问层
 */
@Mapper
public interface RepairOrderEvaluationMapper {
    
    /**
     * 新增工单评价
     */
    int insertEvaluation(RepairOrderEvaluation evaluation);
    
    /**
     * 根据 工单ID 查询评价实体
     */
    RepairOrderEvaluation selectByOrderId(Long orderId);

    /**
     * 根据 工单ID 查询评价展示信息
     */
    RepairOrderEvaluationVO selectEvaluationVOByOrderId(Long orderId);
}
