package com.kory.fixflow.auth.service;

import com.kory.fixflow.auth.dto.LoginDTO;
import com.kory.fixflow.auth.vo.CurrentUserVO;
import com.kory.fixflow.auth.vo.LoginResultVO;

/**
 * 认证业务层接口
 */
public interface AuthService {
    
    /**
     * 用户登录
     * @param loginDTO 登录参数
     * @return 登录结果VO
     */
    LoginResultVO login(LoginDTO loginDTO);
    
    /**
     * 获取当前用户信息
     * @return 当前用户信息VO
     */
    CurrentUserVO getCurrentUserInfo();
}
