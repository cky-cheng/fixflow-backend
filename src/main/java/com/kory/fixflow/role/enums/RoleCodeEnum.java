package com.kory.fixflow.role.enums;

import lombok.Getter;

/**
 * 系统角色编码 枚举
 */
@Getter
public enum RoleCodeEnum {
    
    /*
    管理员
     */
    ADMIN("ADMIN", "管理员"),
    
    /*
    维修人员
     */
    REPAIRER("REPAIRER", "维修人员"),
    
    /*
    普通用户
     */
    USER("USER", "普通用户");
    
    /*
    角色编码
     */
    private final String code;
    
    /*
    角色名称
     */
    private final String name;
    
    RoleCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
    
}
