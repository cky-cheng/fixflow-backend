package com.kory.fixflow.order.service;

import com.kory.fixflow.common.page.PageResult;
import com.kory.fixflow.order.dto.*;
import com.kory.fixflow.order.vo.RepairOrderDetailVO;
import com.kory.fixflow.order.vo.RepairOrderEvaluationVO;
import com.kory.fixflow.order.vo.RepairOrderPageVO;

/**
 * 报修工单业务层接口
 */
public interface RepairOrderService {
    
    /**
     * 提交报修工单
     */
    Long createRepairOrder(CreateRepairOrderDTO createRepairOrderDTO);
    
    
    /**
     * 查询我的工单分页列表
     * @param queryDTO 我的工单分页查询 DTO
     * @return 分页结果
     */
    PageResult<RepairOrderPageVO> pageMyRepairOrders(MyRepairOrderPageQueryDTO queryDTO);
    
    
    /**
     * 根据ID查询工单明细
     * @param id 工单ID
     * @return 工单详情 VO
     */
    RepairOrderDetailVO getRepairOrderDetail(Long id);
    
    
    /**
     * 查询待受理工单分页列表
     * @param queryDTO 管理员查询待受理工单分页 DTO
     * @return 工单分页展示结果
     */
    PageResult<RepairOrderPageVO> pagePendingRepairOrders(AdminPendingRepairOrderPageQueryDTO queryDTO);
    
    
    /**
     * 驳回工单
     * @param id 工单ID
     * @param rejectRepairOrderDTO 驳回工单 DTO
     */
    void rejectRepairOrder(Long id, RejectRepairOrderDTO rejectRepairOrderDTO);
    
    
    /**
     * 派发工单
     * @param id 工单ID
     * @param assignRepairOrderDTO 派发工单 DTO
     */
    void assignRepairOrder(Long id, AssignRepairOrderDTO assignRepairOrderDTO);
    
    
    /**
     * 查询派给我的待接单工单
     * @param queryDTO 维修人员查询分派的待接单工单分页 DTO
     * @return 工单分页展示 VO 分页结果
     */
    PageResult<RepairOrderPageVO> pageWaitReceiveOrders(
            RepairWaitReceivePageQueryDTO queryDTO
    );
    
    
    /**
     * 维修人员接单
     */
    void acceptRepairOrder(Long id);
    
    
    /**
     * 查询我正在处理的工单
     */
    PageResult<RepairOrderPageVO> pageProcessingOrders(
            RepairerProcessingPageQueryDTO queryDTO
    );
    
    /**
     * 完成维修
     */
    void finishRepairOrder(Long id);
    
    
    /**
     * 查询我的待确认工单
     */
    PageResult<RepairOrderPageVO> pageMyWaitConfirmOrders(
            MyWaitConfirmPageQueryDTO queryDTO
    );
    
    /**
     * 用户确认完成
     */
    void confirmRepairOrder(Long id);
    
    
    /**
     * 用户评价工单
     * @param id 工单ID
     * @param evaluationDTO 创建工单评价 DTO
     */
    Long evaluateRepairOrder(Long id, CreateRepairOrderEvaluationDTO evaluationDTO);
    
    
    /**
     * 查询工单评价
     * @param id 工单ID
     * @return 工单评价 VO
     */
    RepairOrderEvaluationVO getRepairOrderEvaluation(Long id);
    
    
    /**
     * 管理员查询全部工单分页
     */
    PageResult<RepairOrderPageVO> pageAdminRepairOrders(AdminRepairOrderPageQueryDTO queryDTO);
    
    
    /**
     * 查询维修人员已完成任务
     */
    PageResult<RepairOrderPageVO> pageRepairCompletedOrders(RepairerCompletedPageQueryDTO queryDTO);
    
}
