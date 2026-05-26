package com.kory.fixflow.log.service;

import com.kory.fixflow.common.page.PageResult;
import com.kory.fixflow.log.dto.OperationLogPageQueryDTO;
import com.kory.fixflow.log.entity.OperationLog;
import com.kory.fixflow.log.vo.OperationLogVO;

/**
 * 操作日志业务层接口
 */
public interface OperationLogService {
    
    /**
     * 保存操作日志
     */
    void saveOperationLog(OperationLog operationLog);
    
    
    /**
     * 分页查询操作日志
     */
    PageResult<OperationLogVO> pageOperationLogs(
            OperationLogPageQueryDTO queryDTO
    );
}
