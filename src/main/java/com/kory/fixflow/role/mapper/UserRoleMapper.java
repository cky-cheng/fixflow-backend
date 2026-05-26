package com.kory.fixflow.role.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联数据访问层
 */
@Mapper
public interface UserRoleMapper {
    
    /**
     * 批量新增用户角色关联
     */
    int insertUserRoles(
            @Param("userId") Long userId,
            @Param("roleIds")List<Long> roleIds
            );
    
    
    /**
     * 删除某个用户的全部角色关联
     */
    int deleteByUserId(Long userId);
    
    
    /**
     * 查询某个用户拥有的角色ID列表
     */
    List<Long> selectRoleIdsByUserId(Long userId);
}
