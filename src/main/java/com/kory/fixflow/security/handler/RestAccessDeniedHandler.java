package com.kory.fixflow.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kory.fixflow.common.enums.ResultCodeEnum;
import com.kory.fixflow.common.result.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 权限不足处理器
 */
@Component
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    
    /**
     * 用于把 Result 对象转换成 JSON 响应
     */
    private final ObjectMapper objectMapper;
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        // HTTP 状态码设置为 403
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        
        // 设置响应类型
        response.setContentType("application/json;charset=UTF-8");
        
        // 构造统一返回结果
        Result<Void> result = Result.fail(ResultCodeEnum.FORBIDDEN);
        
        // 写回 JSON
        objectMapper.writeValue(response.getWriter(), result);
    }
}
