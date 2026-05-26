package com.kory.fixflow.role.mapper;

import com.kory.fixflow.role.entity.Role;
import com.kory.fixflow.role.vo.RoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色数据访问层
 */
@Mapper
public interface RoleMapper {
    
    /**
     * 根据用户ID 查询角色编码列表
     * @param userId 用户ID
     */
    List<String> selectRoleCodesByUserId(Long userId);
    
    /**
     * 查询所有启用角色
     */
    List<RoleVO> selectEnableRoles();
    
    /**
     * 根据角色ID列表查询角色
     */
    List<Role> selectEnabledRolesById(@Param("list") List<Long> roleIds);
}

