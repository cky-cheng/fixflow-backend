package com.kory.fixflow.role.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户角色关联实体表
 */
@Data
public class UserRole {
    
    /*
    主键 ID
     */
    private Long id;
    
    /*
    用户ID
     */
    private Long userId;
    
    /*
    角色ID
     */
    private Long roleId;
    
    /*
    创建时间
     */
    private LocalDateTime createTime;
}
