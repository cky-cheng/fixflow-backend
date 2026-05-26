package com.kory.fixflow.security.filter;

import com.kory.fixflow.common.jwt.JwtUtil;
import com.kory.fixflow.role.mapper.RoleMapper;
import com.kory.fixflow.user.entity.User;
import com.kory.fixflow.user.mapper.UserMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT 认证过滤器
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtUtil;  // JWT 工具类
    
    private final UserMapper userMapper;    // 用户 Mapper
    
    /**
     * 角色 Mapper
     */
    private final RoleMapper roleMapper;
    
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        
        // 获取请求体 Authorization
        String authorizationHeader = request.getHeader("Authorization");
        
        /*
         * 如果没带请求头，或者格式不是 Bearer 开头：
         * 不在这里直接报错，而是继续往后走。
         *
         * 如果访问的是公开接口，例如 /auth/login：
         * 可以正常访问。
         *
         * 如果访问的是受保护接口：
         * 后面的 Spring Security 会判定“未认证”，
         * 再交给 RestAuthenticationEntryPoint 返回 401 JSON。
         */
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            
            filterChain.doFilter(request, response);
            return;
        }
        
        /*
         * 截取真正的 Token 字符串
         */
        String token = authorizationHeader.substring(7);
        
        try {
            /*
             * 解析 Token，获取 userId
             *
             * 如果 Token：
             * - 已过期
             * - 签名错误
             * - 被篡改
             * - 格式不合法
             *
             * 这里会抛出 JwtException 或其他运行时异常。
             */
            Long userId = jwtUtil.getUserId(token);
            
            // 再查一次数据库，确认用户仍然可用
            User user = userMapper.selectById(userId);
            
            // 如果用户不存在或已经禁用     不设置认证信息
            if (user == null || Integer.valueOf(0).equals(user.getStatus())) {
                filterChain.doFilter(request, response);
                return;
            }
            
            // 当前请求如果没有认证信息，才进行设置
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                /*
                 * 查询当前用户拥有的角色编码
                 */
                List<String> roleCodes = roleMapper.selectRoleCodesByUserId(userId);
                
                /*
                 * 转换成 Spring Security 识别的权限对象
                 */
                List<SimpleGrantedAuthority> authorities = roleCodes.stream()
                        .map(roleCode -> new SimpleGrantedAuthority("ROLE_" + roleCode))
                        .toList();
                
                /*
                 * 创建认证对象
                 *  * principal：
                 *  * - 当前阶段仍然放 userId
                 *  *
                 *  * credentials：
                 *  * - JWT 模式不存密码，传 null
                 *  *
                 *  * authorities：
                 *  * - 当前用户角色权限列表
                 */
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userId,
                                null,
                                authorities
                        );
                
                /*
                 * 创建新的 SecurityContext ，并写入认证信息
                 *
                 * Spring Security 官方建议：
                 * 手动设置认证信息时，优先创建新的 SecurityContext，
                 * 而不是直接在旧的 Context 上改，避免线程安全问题
                 */
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                
                securityContext.setAuthentication(authentication);
                
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (JwtException | IllegalArgumentException e) {
            /*
             * Token 无效时：
             * 不在过滤器里直接返回响应
             * 继续交给 Spring Security 判断
             *
             * 如果访问的是受保护接口， 最终返回 401
             */
        }
        /*
         * 继续执行后续过滤链
         */
        filterChain.doFilter(request,response);
    }
}
