package com.kory.fixflow.order.service.impl;

import com.kory.fixflow.asset.entity.Asset;
import com.kory.fixflow.asset.mapper.AssetMapper;
import com.kory.fixflow.common.exception.BusinessException;
import com.kory.fixflow.common.page.PageResult;
import com.kory.fixflow.order.dto.*;
import com.kory.fixflow.order.entity.RepairOrder;
import com.kory.fixflow.order.entity.RepairOrderEvaluation;
import com.kory.fixflow.order.enums.RepairOrderStatusEnum;
import com.kory.fixflow.order.mapper.RepairOrderEvaluationMapper;
import com.kory.fixflow.order.mapper.RepairOrderMapper;
import com.kory.fixflow.order.service.RepairOrderService;
import com.kory.fixflow.order.util.RepairOrderNoUtil;
import com.kory.fixflow.order.vo.RepairOrderDetailVO;
import com.kory.fixflow.order.vo.RepairOrderEvaluationVO;
import com.kory.fixflow.order.vo.RepairOrderPageVO;
import com.kory.fixflow.security.util.SecurityUtil;
import com.kory.fixflow.user.entity.User;
import com.kory.fixflow.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报修工单业务层实现类
 */
@Service
@RequiredArgsConstructor
public class RepairOrderServiceImpl implements RepairOrderService {
    
    /**
     * 工单 Mapper
     */
    private final RepairOrderMapper repairOrderMapper;
    
    /**
     * 用户 Mapper
     */
    private final UserMapper userMapper;
    
    /**
     * 资产 Mapper
     */
    private final AssetMapper assetMapper;
    
    /**
     * 工单评价 Mapper
     */
    private final RepairOrderEvaluationMapper repairOrderEvaluationMapper;
    
    
    /**
     * 提交报修工单
     * @param createRepairOrderDTO 新增报修工单 DTO
     */
    @Override
    public Long createRepairOrder(CreateRepairOrderDTO createRepairOrderDTO) {
        /*
        从当前登录上下文中获取提交人ID
         */
        Long currentUserId = SecurityUtil.getCurrentUserId();
        
        /*
        查询当前登录用户
         */
        User applicant = userMapper.selectById(currentUserId);
        
        if (applicant == null) {
            throw new BusinessException("当前登录用户不存在，请先登录");
        }
        
        /*
        当前用户必须归属部门
         */
        if (applicant.getDepartmentId() == null) {
            throw new BusinessException("当前用户未分配部门，无法提交工单");
        }
        
        /*
        如果前端传来 assetId，则校验资产
         */
        if (createRepairOrderDTO.getAssetId() != null) {
            Asset asset = assetMapper.selectById(createRepairOrderDTO.getAssetId());
            
            if (asset == null) {
                throw new BusinessException("关联资产不存在");
            }
            
            // 资产状态 4 表述已报废 ，   不允许继续提交报修
            if (Integer.valueOf(4).equals(asset.getStatus())) {
                throw new BusinessException("已报废资产不能提交报修工单");
            }
        }
        
        
        /*
        构建工单实体
         */
        RepairOrder repairOrder = new RepairOrder();
        
        repairOrder.setOrderNo(RepairOrderNoUtil.generate());
        repairOrder.setTitle(createRepairOrderDTO.getTitle());
        repairOrder.setDescription(createRepairOrderDTO.getDescription());
        repairOrder.setIssueType(createRepairOrderDTO.getIssueType());
        repairOrder.setUrgency(createRepairOrderDTO.getUrgency());
        /*
        新工单初始状态：待受理
         */
        repairOrder.setStatus(RepairOrderStatusEnum.WAIT_ACCEPT.getCode());
        /*
        提交人及其所属部门
         */
        repairOrder.setApplicantId(currentUserId);
        repairOrder.setDepartmentId(applicant.getDepartmentId());
        /*
        可选关联资产
         */
        repairOrder.setAssetId(createRepairOrderDTO.getAssetId());
        /*
        提交时间
         */
        repairOrder.setSubmitTime(LocalDateTime.now());
        
        /*
        入库
         */
        repairOrderMapper.insertRepairOrder(repairOrder);
        
        /*
        返回新建工单ID
         */
        return repairOrder.getId();
    }
    
    
    /**
     * 查询我的工单分页列表
     * @param queryDTO 我的工单分页查询 DTO
     * @return 分页结果
     */
    @Override
    public PageResult<RepairOrderPageVO> pageMyRepairOrders(MyRepairOrderPageQueryDTO queryDTO) {
        
        // 获取当前用户ID
        Long currentUserId = SecurityUtil.getCurrentUserId();
        
        // 查询当前页数据
        List<RepairOrderPageVO> records = repairOrderMapper.selectMyRepairOrderPageList(currentUserId,queryDTO);
        
        // 查询总数
        Long total = repairOrderMapper.selectMyRepairOrderPageCount(currentUserId, queryDTO);
        
        // 封装分页结果
        return PageResult.of(
                records,
                total,
                queryDTO.getPageNum(),
                queryDTO.getPageSize()
        );
    }
    
    /**
     * 查询工单详情
     * @param id 订单ID
     * @return 工单详情 VO
     */
    @Override
    public RepairOrderDetailVO getRepairOrderDetail(Long id) {
        
        // 获取当前登录用户ID
        Long currentUserId = SecurityUtil.getCurrentUserId();
        
        // 查询工单详情
        RepairOrderDetailVO detailVO = repairOrderMapper.selectMyRepairOrderDetailById(id, currentUserId);
        
        if (detailVO == null) {
            throw new BusinessException("工单不存在");
        }
        
        // 判断当前用户是否是提交人
        boolean isApplicant = currentUserId.equals(detailVO.getApplicantId());
        
        // 判断当前用户是否是被指派维修人员
        boolean isAssignee = detailVO.getAssigneeId() != null && currentUserId.equals(detailVO.getAssigneeId());
        
        // 当前只允许提交人或被指派维修人员查看
        if (!isApplicant && !isAssignee) {
            throw new BusinessException("工单不存在或无权查看");
        }
        
        // 返回详情VO
        return detailVO;
    }
    
    
    /**
     * 查询待受理工单分页列表
     * @param queryDTO 管理员查询待受理工单分页 DTO
     * @return 分页结果
     */
    @Override
    public PageResult<RepairOrderPageVO> pagePendingRepairOrders(AdminPendingRepairOrderPageQueryDTO queryDTO) {
        // 查询当前页数据
        List<RepairOrderPageVO> records = repairOrderMapper.selectPendingRepairOrderPageList(queryDTO);
        
        // 查询总数
        Long total = repairOrderMapper.selectPendingRepairOrderPageCount(queryDTO);
        
        // 封装分页结果
        return PageResult.of(records, total, queryDTO.getPageNum(), queryDTO.getPageSize());
    }
    
    
    /**
     * 驳回工单
     * @param id 工单ID
     * @param rejectRepairOrderDTO 驳回工单 DTO
     */
    @Override
    public void rejectRepairOrder(Long id, RejectRepairOrderDTO rejectRepairOrderDTO) {
        // 查询工单是否存在
        RepairOrder repairOrder = repairOrderMapper.selectById(id);
        
        if (repairOrder == null) {
            throw new BusinessException("工单不存在");
        }
        
        // 只有待受理工单才能驳回
        if (!RepairOrderStatusEnum.WAIT_ACCEPT.getCode().equals(repairOrder.getStatus())) {
            throw new BusinessException("当前工单状态不允许驳回");
        }
        
        // 更新为已驳回（使用乐观锁防止并发）
        int affectedRows = repairOrderMapper.rejectRepairOrder(id,
                RepairOrderStatusEnum.REJECTED.getCode(),
                rejectRepairOrderDTO.getRejectReason(),
                RepairOrderStatusEnum.WAIT_ACCEPT.getCode()
        );

        if (affectedRows == 0) {
            throw new BusinessException("操作失败，工单状态已发生变化，请刷新后重试");
        }
    }
    
    
    /**
     * 派发工单
     * @param id 工单ID
     * @param assignRepairOrderDTO 派发工单 DTO
     */
    @Override
    public void assignRepairOrder(Long id, AssignRepairOrderDTO assignRepairOrderDTO) {
        // 查询工单是否存在
        RepairOrder repairOrder = repairOrderMapper.selectById(id);
        
        if (repairOrder == null) {
            throw new BusinessException("工单不存在");
        }
        
        // 只有待受理工单才能派单
        if (!RepairOrderStatusEnum.WAIT_ACCEPT.getCode().equals(repairOrder.getStatus())) {
            throw new BusinessException("当前工单状态不允许派单");
        }
        
        // 校验被指派用户是否存在
        User assignee = userMapper.selectById(assignRepairOrderDTO.getAssigneeId());
        
        if (assignee == null) {
            throw new BusinessException("维修人员不存在");
        }
        
        if (Integer.valueOf(0).equals(assignee.getStatus())) {
            throw new BusinessException("该维修人员账号已被禁用");
        }
        
        // 更新工单状态为待接单，并记录（使用乐观锁防止并发）
        int affectedRows = repairOrderMapper.assignRepairOrder(
                id,
                RepairOrderStatusEnum.WAIT_RECEIVE.getCode(),
                assignRepairOrderDTO.getAssigneeId(),
                LocalDateTime.now(),
                RepairOrderStatusEnum.WAIT_ACCEPT.getCode()
        );

        if (affectedRows == 0) {
            throw new BusinessException("操作失败，工单状态已发生变化，请刷新后重试");
        }
    }
    
    
    /**
     * 查询派给我的待接单工单
     * @param queryDTO 维修人员查询分派的待接单工单分页 DTO
     */
    @Override
    public PageResult<RepairOrderPageVO> pageWaitReceiveOrders(RepairWaitReceivePageQueryDTO queryDTO) {
        // 获取当前登录用户ID
        Long currentUserId = SecurityUtil.getCurrentUserId();
        
        // 查询当前页数据
        List<RepairOrderPageVO> records = repairOrderMapper.selectWaitReceivePageList(currentUserId, queryDTO);
        
        // 查询总数
        Long total = repairOrderMapper.selectWaitReceivePageCount(currentUserId, queryDTO);
        
        // 封装分页结果
        return PageResult.of(records, total, queryDTO.getPageNum(), queryDTO.getPageSize());
    }
    
    
    /**
     * 维修人员接单
     * @param id 工单ID
     */
    @Override
    public void acceptRepairOrder(Long id) {
        // 获取当前登录用户ID
        Long currentUserId = SecurityUtil.getCurrentUserId();
        
        // 查询工单是否存在
        RepairOrder repairOrder = repairOrderMapper.selectById(id);
        
        if (repairOrder == null) {
            throw new BusinessException("工单不存在");
        }
        
        // 只有待接单状态才能接单
        if (!RepairOrderStatusEnum.WAIT_RECEIVE.getCode().equals(repairOrder.getStatus())) {
            throw new BusinessException("当前工单状态不允许接单");
        }
        
        // 只有被指派的人才能接单
        if (!currentUserId.equals(repairOrder.getAssigneeId())) {
            throw new BusinessException("当前工单不是指派给你的，无法接单");
        }
        
        // 更新状态为处理中，并记录接单时间（使用乐观锁防止并发）
        int affectedRows = repairOrderMapper.acceptRepairOrder(
                id,
                RepairOrderStatusEnum.PROCESSING.getCode(),
                LocalDateTime.now(),
                RepairOrderStatusEnum.WAIT_RECEIVE.getCode()
        );

        if (affectedRows == 0) {
            throw new BusinessException("操作失败，工单状态已发生变化，请刷新后重试");
        }
    }
    
    
    /**
     * 查询我正在完成的工单
     * @param queryDTO 维修人员查询正在处理的工单分页 DTO
     */
    @Override
    public PageResult<RepairOrderPageVO> pageProcessingOrders(RepairerProcessingPageQueryDTO queryDTO) {
        // 获取当前登录用户ID
        Long currentUserId = SecurityUtil.getCurrentUserId();
        
        // 查询当前页数据
        List<RepairOrderPageVO> records = repairOrderMapper.selectProcessingPageList(currentUserId, queryDTO);
        
        // 查询总数
        Long total = repairOrderMapper.selectProcessingPageCount(currentUserId, queryDTO);
        
        // 封装分页结果
        return PageResult.of(
                records,
                total,
                queryDTO.getPageNum(),
                queryDTO.getPageSize()
        );
    }
    
    /**
     * 完成维修
     * @param id 工单ID
     */
    @Override
    public void finishRepairOrder(Long id) {
        // 获取当前登录用户ID
        Long currentUserId = SecurityUtil.getCurrentUserId();
        
        // 查询工单是否存在
        RepairOrder repairOrder = repairOrderMapper.selectById(id);
        
        if (repairOrder == null) {
            throw new BusinessException("工单不存在");
        }
        
        // 只有处理中状态才能完成维修
        if (!RepairOrderStatusEnum.PROCESSING.getCode().equals(repairOrder.getStatus())) {
            throw new BusinessException("当前工单状态不允许完成维修");
        }
        
        // 只有被指派的维修人员才能完成维修
        if (!currentUserId.equals(repairOrder.getAssigneeId())) {
            throw new BusinessException("当前工单不是指派给你的，无法完成维修");
        }
        
        // 更新工单为待确认，并记录完成时间（使用乐观锁防止并发）
        int affectedRows = repairOrderMapper.finishRepairOrder(
                id,
                RepairOrderStatusEnum.WAIT_CONFIRM.getCode(),
                LocalDateTime.now(),
                RepairOrderStatusEnum.PROCESSING.getCode()
        );

        if (affectedRows == 0) {
            throw new BusinessException("操作失败，工单状态已发生变化，请刷新后重试");
        }
    }
    
    
    /**
     * 查询我的待确认工单
     * @param queryDTO 我的待确认工单分页查询 DTO
     */
    @Override
    public PageResult<RepairOrderPageVO> pageMyWaitConfirmOrders(MyWaitConfirmPageQueryDTO queryDTO) {
        // 获取当期登录用户ID
        Long currentUserId = SecurityUtil.getCurrentUserId();
        
        // 查询当前页面数据
        List<RepairOrderPageVO> records = repairOrderMapper.selectMyWaitConfirmPageList(currentUserId, queryDTO);
        
        // 查询总数
        Long total = repairOrderMapper.selectMyWaitConfirmPageCount(currentUserId, queryDTO);
        
        // 封装分页结果
        return PageResult.of(
                records,
                total,
                queryDTO.getPageNum(),
                queryDTO.getPageSize()
        );
    }
    
    
    /**
     * 用户确认完成
     * @param id 工单ID
     */
    @Override
    public void confirmRepairOrder(Long id) {
        // 获取当前登录用户ID
        Long currentUserId = SecurityUtil.getCurrentUserId();
        
        // 查询工单是否存在
        RepairOrder repairOrder = repairOrderMapper.selectById(id);
        
        if (repairOrder == null) {
            throw new BusinessException("工单不存在");
        }
        
        // 订单状态
        if (!RepairOrderStatusEnum.WAIT_CONFIRM.getCode().equals(repairOrder.getStatus())) {
            throw new BusinessException("当前工单状态不允许确认完成");
        }
        
        // 只有提交人本人才能确认完成
        if (!currentUserId.equals(repairOrder.getApplicantId())) {
            throw new BusinessException("当前工单不是你提交的，无法确认完成");
        }
        
        // 更新工单（使用乐观锁防止并发）
        int affectedRows = repairOrderMapper.confirmRepairOrder(
                id,
                RepairOrderStatusEnum.COMPLETED.getCode(),
                LocalDateTime.now(),
                RepairOrderStatusEnum.WAIT_CONFIRM.getCode()
        );

        if (affectedRows == 0) {
            throw new BusinessException("操作失败，工单状态已发生变化，请刷新后重试");
        }
    }
    
    /**
     * 用户评价工单
     * @param id 工单ID
     * @param evaluationDTO 创建工单评价 DTO
     */
    @Override
    @Transactional
    public Long evaluateRepairOrder(
            Long id,
            CreateRepairOrderEvaluationDTO evaluationDTO
    ) {
        // 获取当前登录用户ID
        Long currentUserId = SecurityUtil.getCurrentUserId();
        
        // 查询工单是否存在
        RepairOrder repairOrder = repairOrderMapper.selectById(id);
        
        if (repairOrder == null) {
            throw new BusinessException("工单不存在");
        }
        
        // 只有本人提交才能评价
        if (!currentUserId.equals(repairOrder.getApplicantId())) {
            throw new BusinessException("当前工单不是你提交的，无法评价");
        }
        
        // 只有已完成状态才能评价
        if (!RepairOrderStatusEnum.COMPLETED.getCode().equals(repairOrder.getStatus())) {
            throw new BusinessException("当前工单不允许评价");
        }
        
        /*
        判断是否已经评价过
         */
        RepairOrderEvaluation existEvaluation = repairOrderEvaluationMapper.selectByOrderId(id);
        
        if (existEvaluation != null) {
            throw new BusinessException("该工单已经评价过了");
        }
        
        /*
        构建评价实体
         */
        RepairOrderEvaluation evaluation = new RepairOrderEvaluation();
        evaluation.setOrderId(id);
        evaluation.setScore(evaluationDTO.getScore());
        evaluation.setContent(evaluationDTO.getContent());
        evaluation.setEvaluatorId(currentUserId);
        evaluation.setEvaluateTime(LocalDateTime.now());
        
        // 插入评价记录
        repairOrderEvaluationMapper.insertEvaluation(evaluation);
        
        // 修改工单状态为已评价（使用乐观锁防止并发）
        int affectedRows = repairOrderMapper.markOrderEvaluated(
                id,
                RepairOrderStatusEnum.EVALUATED.getCode(),
                RepairOrderStatusEnum.COMPLETED.getCode()
        );

        if (affectedRows == 0) {
            throw new BusinessException("操作失败，工单状态已发生变化，请刷新后重试");
        }
        
        // 返回评价ID
        return evaluation.getId();
    }
    
    
    /**
     * 查询工单评价
     * @param id 工单ID
     * @return 工单评价 VO
     */
    @Override
    public RepairOrderEvaluationVO getRepairOrderEvaluation(Long id) {
        // 获取当前登录用户ID
        Long currentUserId = SecurityUtil.getCurrentUserId();
        
        // 查询工单是否存在
        RepairOrder repairOrder = repairOrderMapper.selectById(id);
        
        if (repairOrder == null) {
            throw new BusinessException("工单不存在");
        }
        
        // 当前阶段只允许提交人本人查看
        if (!currentUserId.equals(repairOrder.getApplicantId())) {
            throw new BusinessException("工单不存在或无权查看");
        }
        
        // 查询评价信息
        RepairOrderEvaluationVO evaluationVO = repairOrderEvaluationMapper.selectEvaluationVOByOrderId(id);
        
        if (evaluationVO == null) {
            throw new BusinessException("该工单暂无评价");
        }
        
        // 返回评价
        return evaluationVO;
    }
    
    
    /**
     * 管理员查询全部工单分页
     * @param queryDTO 管理员查询全部工单分页 DTO
     */
    @Override
    public PageResult<RepairOrderPageVO> pageAdminRepairOrders(AdminRepairOrderPageQueryDTO queryDTO) {
        
        List<RepairOrderPageVO> records = repairOrderMapper.selectAdminRepairOrderPageList(queryDTO);
        
        Long total = repairOrderMapper.selectAdminRepairOrderPageCount(queryDTO);
        
        return PageResult.of(
                records,
                total,
                queryDTO.getPageNum(),
                queryDTO.getPageSize()
        );
    }
    
    
    /**
     * 查询维修人员已完成任务
     */
    @Override
    public PageResult<RepairOrderPageVO> pageRepairCompletedOrders(RepairerCompletedPageQueryDTO queryDTO) {
        // 获取当前登录维修人员ID
        Long currentUserId = SecurityUtil.getCurrentUserId();
        
        // 查询当前页数据
        List<RepairOrderPageVO> records = repairOrderMapper.selectRepairCompletedPageList(currentUserId, queryDTO);
        
        // 查询总数
        Long total = repairOrderMapper.selectRepairCompletedPageCount(currentUserId, queryDTO);
        
        // 封装分页结果
        return PageResult.of(
                records,
                total,
                queryDTO.getPageNum(),
                queryDTO.getPageSize()
        );
    }
}
