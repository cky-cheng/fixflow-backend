package com.kory.fixflow.role.vo;

import lombok.Data;

/**
 * 角色展示 VO
 */
@Data
public class RoleVO {
    
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
    
    /*
    角色描述
     */
    private String description;
}
