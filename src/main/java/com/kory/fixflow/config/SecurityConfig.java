package com.kory.fixflow.config;

import com.kory.fixflow.security.filter.JwtAuthenticationFilter;
import com.kory.fixflow.security.handler.RestAccessDeniedHandler;
import com.kory.fixflow.security.handler.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security 安全配置类
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    /*
    JWT 认证过滤器
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    /*
    未登录访问处理器
     */
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    
    /*
    权限不足处理器
     */
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    
    /**
     * 密码编码器
     *
     * @return 返回不可逆密文
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS 配置
     *
     * @return CORS 配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许的前端源，开发环境允许所有源
        configuration.setAllowedOriginPatterns(List.of("*"));
        // 允许的请求方法
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 允许的请求头
        configuration.setAllowedHeaders(List.of("*"));
        // 允许携带 Cookie
        configuration.setAllowCredentials(true);
        // 预检请求缓存时间（秒）
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    /**
     * 安全过滤链配置
     *
     * @param http HttpSecurity 配置对象
     * @return 返回安全过滤链
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                /*
                 * 关闭 CSRF
                 *
                 * 当前项目是前后端分离 REST API
                 * 使用 JWT 认证，不使用传统表单 Session
                 */
                .csrf(AbstractHttpConfigurer::disable)

                /*
                 * 配置 CORS
                 *
                 * 前后端分离项目，前端跨域请求需要放行
                 */
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                
                /*
                 * 设置为无状态
                 *
                 * STATELESS
                 * Spring Security 不创建 HttpSession
                 * 也不从 Session 中恢复 SecurityContext
                 */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                /*
                 * 配置未登录时的响应处理
                 */
                .exceptionHandling(exception -> exception
                        // 未登录：401
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        // 已登录但权限不足 403
                        .accessDeniedHandler(restAccessDeniedHandler)
                )
                
                /*
                 * 配置访问规则
                 *
                 * /auth/login
                 *  登录接口必须允许匿名访问
                 * 其他接口
                 *  必须已经认证
                 */
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                
                /*
                 * 把 JWT 过滤器加入 Spring Security 过滤链
                 *
                 * 放在 UsernamePasswordAuthenticationFilter 之前
                 * 这样请求在进入后续授权判断前
                 * 就有机会根据 Token 建立当前认证身份
                 */
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
