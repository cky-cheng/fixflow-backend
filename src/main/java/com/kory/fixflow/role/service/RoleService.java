package com.kory.fixflow.role.service;

import com.kory.fixflow.role.vo.RoleVO;

import java.util.List;

/**
 * 角色业务层接口
 */
public interface RoleService {
    
    /**
     * 查询所有启用角色
     */
    List<RoleVO> listEnabledRoles();
}
