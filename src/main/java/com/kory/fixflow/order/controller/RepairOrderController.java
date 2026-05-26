package com.kory.fixflow.order.controller;

import com.kory.fixflow.common.page.PageResult;
import com.kory.fixflow.common.result.Result;
import com.kory.fixflow.log.annotation.OperationLogAnnotation;
import com.kory.fixflow.order.dto.*;
import com.kory.fixflow.order.service.RepairOrderService;
import com.kory.fixflow.order.vo.RepairOrderDetailVO;
import com.kory.fixflow.order.vo.RepairOrderEvaluationVO;
import com.kory.fixflow.order.vo.RepairOrderPageVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 报修工单控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class RepairOrderController {
    
    private final RepairOrderService repairOrderService;
    
    /**
     * 提交报修工单
     */
    @OperationLogAnnotation(module = "工单管理", operation = "提交工单")
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public Result<Long> createRepairOrder(@Valid @RequestBody CreateRepairOrderDTO repairOrderDTO) {
        Long orderId = repairOrderService.createRepairOrder(repairOrderDTO);
        return Result.success(orderId);
    }
    
    
    /**
     * 查询我的工单分页列表
     * @param queryDTO 我的工单分页查询 DTO
     * @return 分页结果
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my/page")
    public Result<PageResult<RepairOrderPageVO>> pageMyRepairOrders(
            @Valid MyRepairOrderPageQueryDTO queryDTO
            ) {
        PageResult<RepairOrderPageVO> pageResult = repairOrderService.pageMyRepairOrders(queryDTO);
        return Result.success(pageResult);
    }
    
    
    /**
     * 查询工单明细
     * @param id 工单ID
     */
    @GetMapping("/{id}")
    public Result<RepairOrderDetailVO> getRepairOrderDetail(@PathVariable Long id) {
        RepairOrderDetailVO detailVO = repairOrderService.getRepairOrderDetail(id);
        return Result.success(detailVO);
    }
    
    
    /**
     * 查询待受理工单分页
     * @param queryDTO 查询待受理工单分页 DTO
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/pending/page")
    public Result<PageResult<RepairOrderPageVO>> pagePendingRepairOrders(
            @Valid AdminPendingRepairOrderPageQueryDTO queryDTO
            ) {
        PageResult<RepairOrderPageVO> pageResult = repairOrderService.pagePendingRepairOrders(queryDTO);
        
        return Result.success(pageResult);
    }
    
    
    /**
     * 驳回工单
     * @param id 工单ID
     * @param rejectRepairOrderDTO 驳回工单 DTO
     */
    @OperationLogAnnotation(module = "工单管理", operation = "驳回工单")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/reject")
    public Result<Void> rejectRepairOrder(
            @PathVariable Long id,
            @Valid @RequestBody RejectRepairOrderDTO rejectRepairOrderDTO
            ) {
        repairOrderService.rejectRepairOrder(id, rejectRepairOrderDTO);
        return Result.success();
    }
    
    
    /**
     * 派发工单
     * @param id 工单ID
     * @param assignRepairOrderDTO 派发工单 DTO
     */
    @OperationLogAnnotation(module = "工单管理", operation = "派发工单")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/assign")
    public Result<Void> assignRepairOrder(
            @PathVariable Long id,
            @Valid @RequestBody AssignRepairOrderDTO assignRepairOrderDTO
            ) {
        repairOrderService.assignRepairOrder(id, assignRepairOrderDTO);
        return Result.success();
    }
    
    
    /**
     * 查询派给我的待接单工单
     */
    @PreAuthorize("hasRole('REPAIRER')")
    @GetMapping("/repairer/wait-receive/page")
    public Result<PageResult<RepairOrderPageVO>> pageWaitReceiveOrders(
            @Valid RepairWaitReceivePageQueryDTO queryDTO
    ) {
        PageResult<RepairOrderPageVO> pageResult = repairOrderService.pageWaitReceiveOrders(queryDTO);
        return Result.success(pageResult);
    }
    
    
    /**
     * 维修人员接单
     */
    @OperationLogAnnotation(module = "工单管理", operation = "维修人员接单")
    @PreAuthorize("hasRole('REPAIRER')")
    @PutMapping("/{id}/accept")
    public Result<Void> acceptRepairOrder(@PathVariable Long id) {
        repairOrderService.acceptRepairOrder(id);
        return Result.success();
    }
    
    
    /**
     * 查询维修人员正在处理的工单
     */
    @PreAuthorize("hasRole('REPAIRER')")
    @GetMapping("/repairer/processing/page")
    public Result<PageResult<RepairOrderPageVO>> pageProcessingOrders(
            @Valid RepairerProcessingPageQueryDTO queryDTO
    ) {
        PageResult<RepairOrderPageVO> pageResult = repairOrderService.pageProcessingOrders(queryDTO);
        return Result.success(pageResult);
    }


    /**
     * 维修人员完成维修
     */
    @OperationLogAnnotation(module = "工单管理", operation = "完成维修")
    @PreAuthorize("hasRole('REPAIRER')")
    @PutMapping("/{id}/finish")
    public Result<Void> finishRepairOrder(@PathVariable Long id) {
        repairOrderService.finishRepairOrder(id);
        return Result.success();
    }


    /**
     * 查询我的待确认工单
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my/wait-confirm/page")
    public Result<PageResult<RepairOrderPageVO>> pageMyWaitConfirmOrders(
            @Valid MyWaitConfirmPageQueryDTO queryDTO
    ) {
        PageResult<RepairOrderPageVO> pageResult = repairOrderService.pageMyWaitConfirmOrders(queryDTO);
        return Result.success(pageResult);
    }
    
    
    /**
     * 用户确认完成
     */
    @OperationLogAnnotation(module = "工单管理", operation = "用户确认完成")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}/confirm")
    public Result<Void> confirmRepairOrder(
            @PathVariable Long id
    ) {
        repairOrderService.confirmRepairOrder(id);
        return Result.success();
    }
    
    
    /**
     * 用户评价工单
     */
    @OperationLogAnnotation(module = "工单评价", operation = "用户评价工单")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/evaluation")
    public Result<Long> evaluateRepairOrder(
            @PathVariable Long id,
            @Valid @RequestBody CreateRepairOrderEvaluationDTO evaluationDTO
            ) {
        Long evaluationId = repairOrderService.evaluateRepairOrder(id, evaluationDTO);
        return Result.success(evaluationId);
    }
    
    
    /**
     * 查询工单评价
     */
    @GetMapping("/{id}/evaluation")
    public Result<RepairOrderEvaluationVO> getRepairOrderEvaluation(
            @PathVariable Long id
    ) {
        RepairOrderEvaluationVO repairOrderEvaluationVO = repairOrderService.getRepairOrderEvaluation(id);
        return Result.success(repairOrderEvaluationVO);
    }
    
    
    /**
     * 管理员查询全部工单分页
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/page")
    public Result<PageResult<RepairOrderPageVO>> pageAdminRepairOrders(
            @Valid AdminRepairOrderPageQueryDTO queryDTO
    ) {
        PageResult<RepairOrderPageVO> pageResult = repairOrderService.pageAdminRepairOrders(queryDTO);
        return Result.success(pageResult);
    }
    
    
    /**
     * 查询维修人员已完成任务
     */
    @PreAuthorize("hasRole('REPAIRER')")
    @GetMapping("/repairer/completed/page")
    public Result<PageResult<RepairOrderPageVO>> pageRepairerCompletedOrders(
            @Valid RepairerCompletedPageQueryDTO queryDTO
    ) {
        PageResult<RepairOrderPageVO> pageResult = repairOrderService.pageRepairCompletedOrders(queryDTO);
        return Result.success(pageResult);
    }
    
    
}
