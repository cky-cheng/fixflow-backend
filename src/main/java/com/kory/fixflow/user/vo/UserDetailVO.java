package com.kory.fixflow.user.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户详情VO
 */
@Data
public class UserDetailVO {
    private Long id;            // 用户id
    private String username;    // 用户名
    private String realName;    // 真实姓名
    private String phone;       // 手机号
    private String email;       // 邮箱
    
    /*
    所属部门ID
     */
    private Long departmentId;
    
    /*
    部门名称
     */
    private String deptName;
    
    private Integer status;     // 用户状态
    
    /*
    角色ID列表
     */
    private List<Long> roleIds;
    
    /*
    角色编码列表
     */
    private List<String> roleCodes;
    
    private LocalDateTime createTime;   // 创建时间
    
    /*
    更新时间
     */
    private LocalDateTime updateTime;
}
