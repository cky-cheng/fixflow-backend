package com.kory.fixflow.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kory.fixflow.common.enums.ResultCodeEnum;
import com.kory.fixflow.common.result.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 未登录访问受保护接口时的处理器
 */
@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    /**
     * Spring Boot 默认已经集合 Jackson
     * 这里用于把 Result 对象转成 JSON 写回响应体
     */
    private final ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException
    ) throws IOException, ServletException {
        
        // 设置 HTTP 状态码 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        // 设置响应内容类型
        response.setContentType("application/json;charset=UTF-8");
        
        // 构造统一返回结果
        Result<Void> result = Result.fail(ResultCodeEnum.UNAUTHORIZED);
        
        // 输出 JSON
        objectMapper.writeValue(response.getWriter(), result);
    }
}
