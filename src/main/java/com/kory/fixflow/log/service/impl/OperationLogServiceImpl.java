package com.kory.fixflow.log.service.impl;

import com.kory.fixflow.common.page.PageResult;
import com.kory.fixflow.log.dto.OperationLogPageQueryDTO;
import com.kory.fixflow.log.entity.OperationLog;
import com.kory.fixflow.log.mapper.OperationLogMapper;
import com.kory.fixflow.log.service.OperationLogService;
import com.kory.fixflow.log.vo.OperationLogVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志业务层实现类
 */
@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl implements OperationLogService {
    
    private final OperationLogMapper operationLogMapper;
    
    /**
     * 保存操作日志
     * @param operationLog 操作日志实体类
     */
    @Override
    public void saveOperationLog(OperationLog operationLog) {
        
        operationLogMapper.insertOperationLog(operationLog);
    }
    
    
    /**
     * 分页查询操作日志
     * @param queryDTO 操作日志分页查询 DTO
     */
    @Override
    public PageResult<OperationLogVO> pageOperationLogs(OperationLogPageQueryDTO queryDTO) {
        List<OperationLogVO> records = operationLogMapper.selectOperationLogPageList(queryDTO);
        
        Long total = operationLogMapper.selectOperationLogPageCount(queryDTO);
        
        return PageResult.of(
                records,
                total,
                queryDTO.getPageNum(),
                queryDTO.getPageSize()
        );
    }
}
