package com.kory.fixflow.log.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kory.fixflow.common.util.IpUtil;
import com.kory.fixflow.log.annotation.OperationLogAnnotation;
import com.kory.fixflow.log.entity.OperationLog;
import com.kory.fixflow.log.service.OperationLogService;
import com.kory.fixflow.security.util.SecurityUtil;
import com.kory.fixflow.user.entity.User;
import com.kory.fixflow.user.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作日志切面
 *  * 作用：
 *  * 1. 拦截带有 @OperationLogAnnotation 注解的方法；
 *  * 2. 自动记录请求信息；
 *  * 3. 自动记录操作人；
 *  * 4. 自动记录成功或失败；
 *  * 5. 自动记录接口耗时。
 */
@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {
    
    /**
     * 操作日志业务层
     */
    private final OperationLogService operationLogService;
    
    /**
     * 用户 Mapper
     */
    private final UserMapper userMapper;
    
    /**
     * Jackson 对象转换器
     */
    private final ObjectMapper objectMapper;
    
    /**
     * 环绕通知
     *  拦截所有带 @OperationLogAnnotation 注解的方法
     */
    @Around("@annotation(com.kory.fixflow.log.annotation.OperationLogAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        
        // 开始时间
        long startTime = System.currentTimeMillis();
        
        // 先准备日志对象
        OperationLog log = new OperationLog();
        
        // 获取请求对象
        HttpServletRequest request = getRequest();
        
        // 获取注解信息
        OperationLogAnnotation operationLogAnnotation = getOperationLogAnnotation(joinPoint);
        
        if (operationLogAnnotation != null) {
            log.setModule(operationLogAnnotation.module());
            log.setOperation(operationLogAnnotation.operation());
        }
        
        // 填充请求基础信息
        fillRequestInfo(log, joinPoint, request);
        
        try {
            /*
             * 6. 执行目标方法
             *
             * 如果目标方法正常返回，说明操作成功。
             */
            Object result = joinPoint.proceed();
            
            log.setStatus(1);
            log.setErrorMessage(null);
            
            return result;
            
        } catch (Throwable e) {
            
            /*
             * 7. 如果目标方法抛异常，说明操作失败
             */
            log.setStatus(0);
            
            /*
             * 错误信息不要无限长，避免数据库字段过长。
             */
            String errorMessage = e.getMessage();
            
            if (errorMessage != null && errorMessage.length() > 1000) {
                errorMessage = errorMessage.substring(0, 1000);
            }
            
            log.setErrorMessage(errorMessage);
            
            /*
             * 注意：
             * 这里必须继续抛出异常，
             * 让全局异常处理器继续返回原本的错误响应。
             */
            throw e;
            
        } finally {
            
            // 8. 计算耗时
            long costTime = System.currentTimeMillis() - startTime;
            log.setCostTime(costTime);
            
            /*
             * 9. 保存操作日志
             *
             * 日志保存失败不应该影响主业务。
             * 所以这里 catch 掉日志保存异常。
             */
            try {
                operationLogService.saveOperationLog(log);
            } catch (Exception ignored) {
                // 学习阶段先忽略日志保存异常
                // 真实项目中可以写入本地日志文件
            }
        }
    }
    
    /**
     * 获取当前请求对象
     */
    private HttpServletRequest getRequest() {
        
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes == null) {
            return null;
        }
        
        return attributes.getRequest();
    }
    
    /**
     * 获取方法上的 @OperationLogAnnotation 注解
     */
    private OperationLogAnnotation getOperationLogAnnotation(ProceedingJoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();

        return method.getAnnotation(OperationLogAnnotation.class);
    }
    
    /**
     * 填充请求信息
     */
    private void fillRequestInfo(
            OperationLog log,
            ProceedingJoinPoint joinPoint,
            HttpServletRequest request
    ) {
        
        /*
         * 1. Java 方法名
         */
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        
        log.setJavaMethod(className + "." + methodName);
        
        /*
         * 2. 请求信息
         */
        if (request != null) {
            log.setRequestMethod(request.getMethod());
            log.setRequestUri(request.getRequestURI());
            log.setIp(IpUtil.getClientIp(request));
        }
        
        /*
         * 3. 请求参数
         */
        log.setRequestParams(parseRequestParams(joinPoint.getArgs()));
        
        /*
         * 4. 操作人信息
         */
        fillOperatorInfo(log);
    }
    
    /**
     * 填充操作人信息
     */
    private void fillOperatorInfo(OperationLog log) {
        
        try {
            Long currentUserId = SecurityUtil.getCurrentUserId();
            
            log.setOperatorId(currentUserId);
            
            User user = userMapper.selectById(currentUserId);
            
            if (user != null) {
                log.setOperatorUsername(user.getUsername());
            }
            
        } catch (Exception ignored) {
            /*
             * 有些接口可能没有登录用户，例如登录接口。
             * 当前我们暂时不记录登录接口，所以一般不会进这里。
             */
        }
    }
    
    /**
     * 解析请求参数
     */
    private String parseRequestParams(Object[] args) {
        
        try {
            List<Object> filteredArgs = new ArrayList<>();
            
            for (Object arg : args) {
                
                /*
                 * 过滤掉不能序列化或没必要记录的对象。
                 */
                if (arg instanceof HttpServletRequest
                        || arg instanceof HttpServletResponse
                        || arg instanceof MultipartFile) {
                    continue;
                }
                
                filteredArgs.add(arg);
            }
            
            String json = objectMapper.writeValueAsString(filteredArgs);
            
            /*
             * 简单脱敏：
             * 防止某些 DTO 中含 password 字段被直接记录。
             */
            json = maskSensitiveFields(json);
            
            /*
             * 防止参数过长撑爆数据库。
             */
            if (json.length() > 5000) {
                json = json.substring(0, 5000);
            }
            
            return json;
            
        } catch (JsonProcessingException e) {
            return "参数序列化失败";
        }
    }
    
    /**
     * 简单敏感字段脱敏
     *
     * 这里先处理 password 字段。
     */
    private String maskSensitiveFields(String json) {
        
        if (json == null) {
            return null;
        }
        
        return json.replaceAll(
                "(?i)(\"password\"\\s*:\\s*\")[^\"]*(\")",
                "$1******$2"
        );
    }
}
