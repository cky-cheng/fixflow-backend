package com.kory.fixflow.auth.vo;

import lombok.Data;

import java.util.List;

/**
 * 登录返回结果 VO
 */
@Data
public class LoginResultVO {
    private Long userId;    // 用户ID
    private String username;    // 登录账号
    private String realName;    // 真实姓名
    
    private String token;   // 登录成功后签发的 JWT Token
    
    /*
    当前用户拥有的角色编码列表
     */
    private List<String> roles;
}
