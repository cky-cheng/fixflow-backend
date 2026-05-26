package com.kory.fixflow.role.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色实体类
 */
@Data
public class Role {
    
    /*
    角色ID
     */
    private Long id;
    
    /*
    角色名称
     */
    private String roleName;
    
    /*
    角色编码
     */
    private String roleCode;
    
    /**
     * 角色描述
     */
    private String description;
    
    /*
    状态
     */
    private Integer status;
    
    /*
    逻辑删除
     */
    private Integer isDeleted;
    
    /*
    创建时间
     */
    private LocalDateTime createTime;
    
    /*
    更新时间
     */
    private LocalDateTime updateTime;
}
