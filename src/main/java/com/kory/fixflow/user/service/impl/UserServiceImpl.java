package com.kory.fixflow.user.service.impl;

import com.kory.fixflow.common.exception.BusinessException;
import com.kory.fixflow.common.page.PageResult;
import com.kory.fixflow.role.entity.Role;
import com.kory.fixflow.role.mapper.RoleMapper;
import com.kory.fixflow.role.mapper.UserRoleMapper;
import com.kory.fixflow.user.constant.UserConstants;
import com.kory.fixflow.user.dto.*;
import com.kory.fixflow.user.entity.User;
import com.kory.fixflow.user.mapper.UserMapper;
import com.kory.fixflow.user.service.UserService;
import com.kory.fixflow.user.vo.UserDetailVO;
import com.kory.fixflow.user.vo.UserPageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户业务层实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    /*
    用户 Mapper
     */
    private final UserMapper userMapper;
    
    /*
    角色 Mapper
     */
    private final RoleMapper roleMapper;
    
    /*
    用户角色 Mapper
     */
    private final UserRoleMapper userRoleMapper;
    
    /**
     * 密码编码器
     * *
     * 新增用户
     * 重置密码
     * 登录
     */
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 分页展示
     * @param queryDTO 分页查询条件
     * @return 返回 统一分页结果
     */
    @Override
    public PageResult<UserPageVO> pageUsers(UserPageQueryDTO queryDTO) {
        
        // 查询当前页面的数据列表
        List<UserPageVO> records = userMapper.selectUserPageList(queryDTO);
        
        // 查询符合条件的数据总数
        Long total = userMapper.selectUserPageCount(queryDTO);
        
        // 统一分页结果返回
        return PageResult.of(records, total, queryDTO.getPageNum(), queryDTO.getPageSize());
        
    }
    
    /**
     * 新增用户
     * @param createUserDTO 新增用户 DTO
     * @return 返回ID
     */
    @Override
    @Transactional
    public Long createUser(CreateUserDTO createUserDTO) {
        
        // 根据用户名查询数据库，校验用户名是否已存在
        User existUsernameUser = userMapper.selectByUsername(createUserDTO.getUsername());
        
        if (existUsernameUser != null) {
            throw new BusinessException("用户名已经存在");
        }
        
        // 校验手机号是否存在
        User existPhoneUser = userMapper.selectByPhone(createUserDTO.getPhone());
        
        if (existPhoneUser != null) {
            throw new BusinessException("手机号已存在");
        }
        
        /*
        校验角色ID是否合法
         */
        validateRoleIds(createUserDTO.getRoleIds());
        
        // DTO 转 Entity
        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        
        // 前端传来的明文密码编码成 BCrypt 密文入库
        String encodedPassword = passwordEncoder.encode(createUserDTO.getPassword());
        user.setPassword(encodedPassword);
        user.setRealName(createUserDTO.getRealName());
        user.setPhone(createUserDTO.getPhone());
        user.setEmail(createUserDTO.getEmail());
        
        // 设置部门ID
        user.setDepartmentId(createUserDTO.getDepartmentId());
        
        // 新增用户默认启用
        user.setStatus(1);
        
        // 插入用户
        userMapper.insertUser(user);
        
        // 插入用户角色关联
        userRoleMapper.insertUserRoles(user.getId(), createUserDTO.getRoleIds());
        
        // 数据库自增主键回填 user.id
        return user.getId();
    }
    
    /**
     * 修改用户信息
     * @param id 用户ID
     * @param updateUserDTO 更新用户 DTO
     */
    @Override
    @Transactional
    public void updateUser(Long id, UpdateUserDTO updateUserDTO) {
        
        // 根据 id 查询用户
        User user = userMapper.selectById(id);
        
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 根据手机号查询数据库中是否已有用户
        User phoneUser = userMapper.selectByPhone(updateUserDTO.getPhone());
        
        // 判断手机号是否冲突
        if (phoneUser != null && !phoneUser.getId().equals(id)) {
            throw new BusinessException("手机号已被其他用户使用");
        }
        
        // 更新用户信息
        user.setRealName(updateUserDTO.getRealName());
        user.setPhone(updateUserDTO.getPhone());
        user.setEmail(updateUserDTO.getEmail());
        user.setDepartmentId(updateUserDTO.getDepartmentId());

        // 执行更新
        userMapper.updateUser(user);
    }
    
    /**
     * 逻辑删除用户
     * @param id 用户ID
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userMapper.selectById(id);

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 清理用户角色关联
        userRoleMapper.deleteByUserId(id);

        // 逻辑删除用户
        userMapper.deleteUserById(id);
    }
    
    /**
     * 修改用户状态
     * @param id 用户ID
     * @param updateUserStatusDTO 更新用户状态 DTO
     */
    @Override
    public void updateUserStatus(Long id, UpdateUserStatusDTO updateUserStatusDTO) {
        User user = userMapper.selectById(id);
        
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        userMapper.updateUserStatus(id, updateUserStatusDTO.getStatus());
    }
    
    /**
     * 查找用户详情
     * @param id 用户ID
     * @return 返回用户详情VO
     */
    @Override
    public UserDetailVO getUserDetail(Long id) {
        // 查询用户基础详情
        UserDetailVO userDetailVO = userMapper.selectUserDetailById(id);
        
        if (userDetailVO == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 查询角色ID列表
        List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(id);
        
        // 查询角色编码列表
        List<String> roleCodes = roleMapper.selectRoleCodesByUserId(id);
        
        // 填充角色信息
        userDetailVO.setRoleIds(roleIds);
        userDetailVO.setRoleCodes(roleCodes);
        
        return userDetailVO;
    }
    
    
    /**
     * 修改用户角色
     */
    @Override
    @Transactional
    public void updateUserRoles(Long id, UpdateUserRolesDTO rolesDTO) {
        
        // 用户必须存在
        User user = userMapper.selectById(id);
        
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 角色必须有效
        validateRoleIds(rolesDTO.getRoleIds());
        
        // 删除旧角色
        userRoleMapper.deleteByUserId(id);
        
        // 插入新角色
        userRoleMapper.insertUserRoles(id, rolesDTO.getRoleIds());
    }
    
    /**
     * 重置用户密码
     * @param id 用户ID
     */
    @Override
    public void resetPassword(Long id) {
        User user = userMapper.selectById(id);
        
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 对默认密码进行 BCrypt 编码
        String encodedPassword = passwordEncoder.encode(UserConstants.DEFAULT_RESET_PASSWORD);
        
        // 重置为默认密码
        userMapper.updatePassword(id, encodedPassword);
    }
    
    
    /**
     * 校验角色ID列表是否合法
     */
    private void validateRoleIds(List<Long> roleIds) {
        
        if (roleIds == null || roleIds.isEmpty()) {
            throw new BusinessException("用户角色不能为空");
        }
        
        List<Role> roles = roleMapper.selectEnabledRolesById(roleIds);
        
        if (roles == null || roles.size() != roleIds.size()) {
            throw new BusinessException("存在无效或已禁用角色");
        }
    }
}
