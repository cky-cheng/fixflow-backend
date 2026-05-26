package com.kory.fixflow.log.controller;

import com.kory.fixflow.common.page.PageResult;
import com.kory.fixflow.common.result.Result;
import com.kory.fixflow.log.dto.OperationLogPageQueryDTO;
import com.kory.fixflow.log.service.OperationLogService;
import com.kory.fixflow.log.vo.OperationLogVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/operation-logs")
@PreAuthorize("hasRole('ADMIN')")
public class OperationLogController {
    
    private final OperationLogService operationLogService;
    
    /**
     * 分页查询操作日志
     */
    @GetMapping("/page")
    public Result<PageResult<OperationLogVO>> pageOperationLogs(
            @Valid OperationLogPageQueryDTO queryDTO
            ) {
        PageResult<OperationLogVO> pageResult = operationLogService.pageOperationLogs(queryDTO);
        return Result.success(pageResult);
    }
}
