package com.kory.fixflow;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 临时密码编码测试类
 */
public class PasswordEncoderTest {
    
    @Test
    void printEncodedPassword() {
        // 创建 BCrypt 编码器
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        // 对默认密码进行编码
        String encodedPassword = passwordEncoder.encode("123456");
        
        // 控制台打印编码结果
        System.out.println(encodedPassword);
    }
}
