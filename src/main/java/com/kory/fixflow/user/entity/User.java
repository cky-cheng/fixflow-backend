package com.kory.fixflow.user.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
public class User {
    private Long id;            // 用户id
    private String username;    // 用户名
    private String password;    // 密码
    private String realName;    // 真实姓名
    private String phone;       // 手机号
    private String email;       // 邮箱
    
    private Long departmentId;  // 所属部门ID
    
    private Integer status;     // 状态
    private Integer isDeleted;  // 删除
    private LocalDateTime createTime;   // 创建时间
    private LocalDateTime updateTime;   // 更新时间
}
