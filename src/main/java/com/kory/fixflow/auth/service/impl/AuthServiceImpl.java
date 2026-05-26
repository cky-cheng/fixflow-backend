package com.kory.fixflow.auth.service.impl;

import com.kory.fixflow.auth.dto.LoginDTO;
import com.kory.fixflow.auth.service.AuthService;
import com.kory.fixflow.auth.vo.CurrentUserVO;
import com.kory.fixflow.auth.vo.LoginResultVO;
import com.kory.fixflow.common.exception.BusinessException;
import com.kory.fixflow.common.jwt.JwtUtil;
import com.kory.fixflow.role.mapper.RoleMapper;
import com.kory.fixflow.security.util.SecurityUtil;
import com.kory.fixflow.user.entity.User;
import com.kory.fixflow.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 认证业务层实现类
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final UserMapper userMapper;    // 用户数据访问层
    
    private final PasswordEncoder passwordEncoder;  // 密码编码器
    
    private final JwtUtil jwtUtil;  // 注入 JwtUtil
    
    /*
    角色 Mapper
     */
    private final RoleMapper roleMapper;
    
    /**
     * 用户登录
     * @param loginDTO 登录参数
     * @return 登录结果 VO
     */
    @Override
    public LoginResultVO login(LoginDTO loginDTO) {
        // 根据用户名查询可登录用户
        User user = userMapper.selectLoginUserByUsername(loginDTO.getUsername());
        
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        
        // 使用 passwordEncoder.matches 校验密码
        boolean passwordMatched = passwordEncoder.matches(
                loginDTO.getPassword(), user.getPassword()
        );
        
        // 密码不匹配    统一提示
        if (!passwordMatched) {
            throw new BusinessException("用户名或密码错误");
        }
        
        
        /*
        查询当前用户角色
         */
        List<String> roleCodes = roleMapper.selectRoleCodesByUserId(user.getId());
        
        /*
        如果没有角色，直接禁止登录
         */
        if (roleCodes == null || roleCodes.isEmpty()) {
            throw new BusinessException("当前用户未分配角色， 请联系管理员");
        }
        
        // 登录成功，生成 JWT Toke
        String token = jwtUtil.generateToken(user);
        
        // 封装返回结果
        LoginResultVO loginResultVO = new LoginResultVO();
        loginResultVO.setUserid(user.getId());
        loginResultVO.setUsername(user.getUsername());
        loginResultVO.setRealName(user.getRealName());
        loginResultVO.setToken(token);
        loginResultVO.setRoles(roleCodes);
        
        return loginResultVO;
    }
    
    /**
     * 获取当前登录用户信息
     * @return 当前用户信息VO
     */
    @Override
    public CurrentUserVO getCurrentUserInfo() {
        /*
         * 1. 从 Spring Security 上下文中获取当前登录用户ID
         *
         * 注意：
         * 不需要前端传 userId；
         * userId 来自 Token 解析后的认证上下文。
         */
        Long currentUserId = SecurityUtil.getCurrentUserId();
        
        /*
         * 2. 查询数据库中的用户信息
         *
         * selectById 已经包含：
         * AND is_deleted = 0
         *
         * 所以已删除用户会查不到。
         */
        User user= userMapper.selectById(currentUserId);
        
        /*
         * 3. 理论上过滤器已经做过用户存在性校验
         * 这里兜底一次
         */
        if (user == null) {
            throw new BusinessException("当前用户不存在");
        }
        
        /*
         * 查询当前用户角色
         */
        List<String> roleCodes = roleMapper.selectRoleCodesByUserId(currentUserId);
        
        /*
         * 如果没有角色，说明账号权限数据异常
         */
        if (roleCodes == null || roleCodes.isEmpty()) {
            throw new BusinessException("当前用户未分配角色，请联系管理员");
        }
        
        /*
         * 4. 封装返回 VO
         */
        CurrentUserVO currentUserVO = new CurrentUserVO();
        currentUserVO.setUserId(user.getId());
        currentUserVO.setUsername(user.getUsername());
        currentUserVO.setRealName(user.getRealName());
        currentUserVO.setRoles(roleCodes);
        
        return currentUserVO;
    }
}
