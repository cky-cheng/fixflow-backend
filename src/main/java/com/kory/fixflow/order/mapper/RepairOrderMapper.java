package com.kory.fixflow.order.mapper;

import com.kory.fixflow.order.dto.*;
import com.kory.fixflow.order.entity.RepairOrder;
import com.kory.fixflow.order.vo.RepairOrderDetailVO;
import com.kory.fixflow.order.vo.RepairOrderPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报修工单数据访问层
 */
@Mapper
public interface RepairOrderMapper {
    
    /**
     * 新增报修工单
     */
    int insertRepairOrder(RepairOrder repairOrder);
    
    /**
     * 查询当前登录用户提交的工单分页列表
     * @param currentUserId 当前用户ID
     * @param queryDTO 我的工单分页查询 DTO
     * @return 工单分页列表
     */
    List<RepairOrderPageVO> selectMyRepairOrderPageList(
            @Param("currentUserId") Long currentUserId,
            @Param("queryDTO") MyRepairOrderPageQueryDTO queryDTO
            );
    
    
    /**
     * 查询当前登录用户提交的工单总数
     * @param currentUserId 当前用户ID
     * @param queryDTO 我的工单分页查询 DTO
     * @return 我的工单总数
     */
    Long selectMyRepairOrderPageCount(
            @Param("currentUserId") Long currentUserId,
            @Param("queryDTO") MyRepairOrderPageQueryDTO queryDTO
    );
    
    
    /**
     * 查询当前有权查看的工单详情
     * @param id 工单ID
     * @param currentUserId 当前用户ID
     * @return 工单详情 VO
     */
    RepairOrderDetailVO selectMyRepairOrderDetailById(
            @Param("id") Long id,
            @Param("currentUserId") Long currentUserId
    );
    
    
    /**
     * 查询待受理工单分页列表
     * @param queryDTO 查询待受理工单分页 DTO
     * @return 工单分页展示 VO
     */
    List<RepairOrderPageVO> selectPendingRepairOrderPageList(
            AdminPendingRepairOrderPageQueryDTO queryDTO
    );
    
    /**
     * 查询待受理工单总数
     * @param queryDTO 查询待受理工单分页 DTO
     */
    Long selectPendingRepairOrderPageCount(
            AdminPendingRepairOrderPageQueryDTO queryDTO
    );
    
    /**
     * 根据工单ID查询工单实体
     * @param id 工单ID
     * @return 工单实体
     */
    RepairOrder selectById(Long id);
    
    /**
     * 驳回工单
     * @param id 工单ID
     * @param status 工单状态
     * @param rejectReason 驳回理由
     * @param expectedStatus 期望的当前状态（乐观锁）
     */
    int rejectRepairOrder(
            @Param("id") Long id,
            @Param("status") Integer status,
            @Param("rejectReason") String rejectReason,
            @Param("expectedStatus") Integer expectedStatus
    );

    /**
     * 派发工单
     * @param id 工单ID
     * @param status 工单状态
     * @param assigneeId 被派发人员ID
     * @param assigneeTime 派发时间
     * @param expectedStatus 期望的当前状态（乐观锁）
     */
    int assignRepairOrder(
            @Param("id") Long id,
            @Param("status") Integer status,
            @Param("assigneeId") Long assigneeId,
            @Param("assigneeTime") LocalDateTime assigneeTime,
            @Param("expectedStatus") Integer expectedStatus
            );
    
    
    /**
     * 查询派给当前维修人员的待接单工单分页列表
     */
    List<RepairOrderPageVO> selectWaitReceivePageList(
            @Param("currentUserId") Long currentUserId,
            @Param("queryDTO")RepairWaitReceivePageQueryDTO queryDTO
            );
    
    /**
     * 查询派给当前维修人员的待接单工单分页总数
     */
    Long selectWaitReceivePageCount(
            @Param("currentUserId") Long currentUserId,
            @Param("queryDTO") RepairWaitReceivePageQueryDTO queryDTO
    );
    
    /**
     * 维修人员接单
     */
    int acceptRepairOrder(
            @Param("id") Long id,
            @Param("status") Integer status,
            @Param("acceptTime") LocalDateTime acceptTime,
            @Param("expectedStatus") Integer expectedStatus
    );
    
    
    /**
     * 查询当前维修人员正在处理的工单分类列表
     */
    List<RepairOrderPageVO> selectProcessingPageList(
            @Param("currentUserId") Long currentUserId,
            @Param("queryDTO") RepairerProcessingPageQueryDTO queryDTO
            );
    
    /**
     * 查询当前维修人员正在处理的工单总数
     */
    Long selectProcessingPageCount(
            @Param("currentUserId") Long currentUserId,
            @Param("queryDTO") RepairerProcessingPageQueryDTO queryDTO
    );
    
    /**
     * 完成维修
     */
    int finishRepairOrder(
            @Param("id") Long id,
            @Param("status") Integer status,
            @Param("finishTime") LocalDateTime finishTime,
            @Param("expectedStatus") Integer expectedStatus
    );
    
    
    /**
     * 查询当前用户待确认的工单分页列表
     */
    List<RepairOrderPageVO> selectMyWaitConfirmPageList(
            @Param("currentUserId") Long currentUserId,
            @Param("queryDTO") MyWaitConfirmPageQueryDTO queryDTO
    );
    
    
    /**
     * 查询当前用户待确认的工单总数
     */
    Long selectMyWaitConfirmPageCount(
            @Param("currentUserId") Long currentUserId,
            @Param("queryDTO") MyWaitConfirmPageQueryDTO queryDTO
    );
    
    /**
     * 用户确认完成
     */
    int confirmRepairOrder(
            @Param("id") Long id,
            @Param("status") Integer status,
            @Param("confirmTime") LocalDateTime confirmTime,
            @Param("expectedStatus") Integer expectedStatus
    );


    /**
     * 标记工单已评价
     */
    int markOrderEvaluated(
            @Param("id") Long id,
            @Param("status") Integer status,
            @Param("expectedStatus") Integer expectedStatus
    );
    
    
    /**
     * 管理查询全部工单分页列表
     */
    List<RepairOrderPageVO> selectAdminRepairOrderPageList(
            AdminRepairOrderPageQueryDTO queryDTO
    );
    
    /**
     * 管理员查询全部工单总数
     */
    Long selectAdminRepairOrderPageCount(
            AdminRepairOrderPageQueryDTO queryDTO
    );
    
    /**
     * 维修人员查询已完成任务分页列表
     */
    List<RepairOrderPageVO> selectRepairCompletedPageList(
            @Param("currentUserId") Long currentUserId,
            @Param("queryDTO") RepairerCompletedPageQueryDTO queryDTO
    );
    
    /**
     * 维修人员已完成任务总数
     */
    Long selectRepairCompletedPageCount(
            @Param("currentUserId") Long currentUserId,
            @Param("queryDTO") RepairerCompletedPageQueryDTO queryDTO
    );
}
