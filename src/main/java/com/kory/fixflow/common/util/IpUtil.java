package com.kory.fixflow.common.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * IP 工具类
 */
public class IpUtil {
    
    /**
     * 获取客户端 IP
     */
    public static String getClientIp(HttpServletRequest request) {
        // 获取ip
        String ip = request.getHeader("X-Forwarded-For");
        
        if (isEmptyIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        
        if (isEmptyIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        
        if (isEmptyIp(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        
        if (isEmptyIp(ip)) {
            ip = request.getRemoteAddr();
        }
        
        /*
         * X-Forwarded-For 可能是：
         * client, proxy1, proxy2
         *
         * 取第一个作为真实客户端 IP。
         */
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
    
    public static boolean isEmptyIp(String ip) {
        return ip == null
                || ip.isBlank()
                || "unknown".equalsIgnoreCase(ip);
    }
    
    private IpUtil(){
    
    }
}
