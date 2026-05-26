package com.kory.fixflow.order.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 工单编号生成工具类
 * *
 * 编号规则：
 * R0 + yyyyMMddHHmmss + 6位随机数
 * *
 * 示例：
 * R020260518153045123456
 */

public class RepairOrderNoUtil {
    
    // 日期时间格式化器
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    // 生成工单编号
    public static String generate() {
        
        // 当前时间
        String timePart = LocalDateTime.now().format(FORMATTER);
        
        // 生成 6 位随机数
        int randomPart = ThreadLocalRandom.current().nextInt(100000, 1000000);
        
        // 拼接最终编号
        return "RO" + timePart + randomPart;
    }
    
    /**
     * 工具类不允许外部实例化
     */
    private RepairOrderNoUtil() {
    }
}
