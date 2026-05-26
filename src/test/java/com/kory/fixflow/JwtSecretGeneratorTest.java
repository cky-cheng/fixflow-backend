package com.kory.fixflow;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

/**
 * JWT 密钥生成测试类
 */
public class JwtSecretGeneratorTest {
    @Test
    void generateJwtSecret() {
        
        // 生成适合 HS256 的密钥
        SecretKey secretKey = Jwts.SIG.HS256.key().build();
        
        // 编码成 Base64 字符串
        String base64Secret = Encoders.BASE64.encode(secretKey.getEncoded());
        
        // 打印到控制台
        System.out.println(base64Secret);
    }
}
