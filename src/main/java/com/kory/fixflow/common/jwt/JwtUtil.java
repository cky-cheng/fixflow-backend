package com.kory.fixflow.common.jwt;

import com.kory.fixflow.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT 工具类
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {
    
    // JWT 配置
    private final JwtProperties jwtProperties;
    
    /**
     * 生成 JWT Token
     * @param user  用户实体
     * @return 字符串
     */
    public String generateToken(User user) {
        
        // 当前时间
        Date now = new Date();
        
        // 过期时间
        Date expirationDate = new Date(
                now.getTime() + jwtProperties.getExpiration()
        );
        
        /*
         * 3. 构建并签发 JWT
         *
         * subject：
         * - 我们放 userId
         * - 后续解析 Token 时，可直接拿到当前登录用户 ID
         *
         * claim("username")：
         * - 附带用户名
         * - 便于调试与后续扩展
         *
         * issuedAt：
         * - 签发时间
         *
         * expiration：
         * - 过期时间
         */
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))      // 存userId
                .claim("username", user.getUsername())  // 作为自定义 Claim 存进去，便于后续扩展
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSecretKey())
                .compact();
    }
    
    
    /**
     * 解析并校验 Token
     *
     * @param token JWT 字符串
     * @return Claims 载荷
     */
    public Claims parseToken(String token) {
        
        return Jwts.parser()
                .verifyWith(getSecretKey())     // 校验签名
                .build()
                .parseSignedClaims(token)   // 解析带签名、Claims 载荷的 JWT
                .getPayload();
        
    }
    
    /**
     * 从 token 中获取userId
     */
    public Long getUserId(String token) {
        
        Claims claims = parseToken(token);
        
        String subject = claims.getSubject();
        
        return Long.valueOf(subject);
    }
    
    /**
     * 将配置文件中的 Base64 密钥转换成 SecretKey
     * *
     * JJWT 官方推荐：
     * Base64 字符串 → 解码成 byte[]
     * → Keys.hmacShaKeyFor(...)
     */
    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    
    
    
}
