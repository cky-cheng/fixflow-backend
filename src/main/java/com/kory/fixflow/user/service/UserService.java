package com.kory.fixflow.user.service;

import com.kory.fixflow.common.page.PageResult;
import com.kory.fixflow.user.dto.*;
import com.kory.fixflow.user.vo.UserDetailVO;
import com.kory.fixflow.user.vo.UserPageVO;

/**
 * 用户业务层接口
 */
public interface UserService {
    
    /**
     * 用户分页查询
     * @param queryDTO 分页查询条件
     * @return 返回 统一分页结果
     */
    PageResult<UserPageVO> pageUsers(UserPageQueryDTO queryDTO);

    /**
     * 新增用户
     * @param createUserDTO 新增用户 DTO
     * @return 返回用户ID
     */
    Long createUser(CreateUserDTO createUserDTO);

    /**
     * 更新用户
     * @param id 用户ID
     * @param updateUserDTO 更新用户 DTO
     */
    void updateUser(Long id, UpdateUserDTO updateUserDTO);

    /**
     * 逻辑删除用户
     * @param id 用户ID
     */
    void deleteUser(Long id);

    /**
     * 修改用户状态
     * @param id 用户ID
     * @param updateUserStatusDTO 更新用户状态 DTO
     */
    void updateUserStatus(Long id, UpdateUserStatusDTO updateUserStatusDTO);
    
    /**
     * 查询用户详情
     * @param id 用户ID
     * @return 返回用户详情VO
     */
    UserDetailVO getUserDetail(Long id);
    
    
    /**
     * 修改用户角色
     */
    void updateUserRoles(Long id, UpdateUserRolesDTO rolesDTO);
    
    
    /**
     * 重置用户密码
     * @param id 用户ID
     */
    void resetPassword(Long id);
}
