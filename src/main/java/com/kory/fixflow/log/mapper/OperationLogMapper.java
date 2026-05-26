package com.kory.fixflow.log.mapper;

import com.kory.fixflow.log.dto.OperationLogPageQueryDTO;
import com.kory.fixflow.log.entity.OperationLog;
import com.kory.fixflow.log.vo.OperationLogVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 操作日志数据访问层
 */
@Mapper
public interface OperationLogMapper {
    
    /**
     * 新增操作日志
     */
    int insertOperationLog(OperationLog operationLog);
    
    /**
     * 分页查询操作日志列表
     */
    List<OperationLogVO> selectOperationLogPageList(
            OperationLogPageQueryDTO queryDTO
    );
    
    /**
     * 查询操作日志总数
     */
    Long selectOperationLogPageCount(
            OperationLogPageQueryDTO queryDTO
    );
}
