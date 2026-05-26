package com.kory.fixflow.security.util;

import com.kory.fixflow.common.enums.ResultCodeEnum;
import com.kory.fixflow.common.exception.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全上下文工具类
 * *
 * 作用：
 * 1. 从 Spring Security 的 SecurityContext 中获取当前登录用户；
 * 2. 封装公共逻辑，避免每个业务模块都重复写一遍。
 * *
 * 当前项目中：
 * - JwtAuthenticationFilter 把 userId 放到了 Authentication.principal 中；
 * - 所以这里直接从 principal 里取 Long 类型的 userId。
 */
public class SecurityUtil {
    /*
     * 获取当前登录用户ID
     */
    public static Long getCurrentUserId() {
        /*
         * 1. 从 SecurityContextHolder 中获取当前请求的 Authentication
         *
         * Spring Security 会把当前认证信息保存在 SecurityContext 中。
         */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        /*
         * 2. 认证信息不存在，说明当前请求不存在
         */
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }
        
        /*
         * 3. 获取 principal
         *
         * 我们在 JwtAuthenticationFilter 中放进去的是 userId：
         * new UsernamePasswordAuthenticationToken(userId, null, ...)
         */
        Object principal = authentication.getPrincipal();
        
        /*
         * 4. 做类型判断，避免转换异常
         */
        if (!(principal instanceof Long)) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }
        
        /*
        5. 返回当前登录用户 id
         */
        return (Long) principal;
    }
    
    /**
     * 私有构造方法
     * 工具类不允许外部 new
     */
    private SecurityUtil() {
    }
}
