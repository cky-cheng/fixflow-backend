package com.kory.fixflow.auth.vo;

import lombok.Data;

import java.util.List;

/**
 * 当前登录用户信息 VO
 */
@Data
public class CurrentUserVO {
    private Long userId;    // 用户id
    private String username;    // 用户名
    private String realName;    // 真实姓名
    
    /*
    当前用户拥有的角色编码列表
     */
    private List<String> roles;
}
