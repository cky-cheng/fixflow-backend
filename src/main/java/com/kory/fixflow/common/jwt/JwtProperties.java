package com.kory.fixflow.common.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 配置属性类
 * *
 * 自动读取 application.yml
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    @NotBlank(message = "JWT 密钥不能为空")
    private String secret;  // JWT 签名密钥

    @NotNull(message = "JWT 过期时间不能为空")
    @Positive(message = "JWT 过期时间必须为正数")
    private Long expiration;    // Token 过期时间，单位毫秒
}
