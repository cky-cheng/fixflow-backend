package com.kory.fixflow.user.controller;

import com.kory.fixflow.common.page.PageResult;
import com.kory.fixflow.common.result.Result;
import com.kory.fixflow.log.annotation.OperationLogAnnotation;
import com.kory.fixflow.user.dto.*;
import com.kory.fixflow.user.service.UserService;
import com.kory.fixflow.user.vo.UserDetailVO;
import com.kory.fixflow.user.vo.UserPageVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
@Validated
public class UserController {
    
    private final UserService userService;
    
    /**
     * 用户分页查询接口
     * @param userPageQueryDTO 用户分页DTO
     * @return 统一返回分页结果
     */
    @GetMapping("/users/page")
    public Result<PageResult<UserPageVO>> pageUser(@Valid UserPageQueryDTO userPageQueryDTO) {
        
        PageResult<UserPageVO> pageResult = userService.pageUsers(userPageQueryDTO);
        
        return Result.success(pageResult);
    }
    
    
    /**
     * 新增用户接口
     * @param createUserDTO 新增用户DTO
     * @return 有数据返回
     */
    @OperationLogAnnotation(module = "用户管理", operation = "新增用户")
    @PostMapping("/users")
    public Result<Long> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        
        Long userId = userService.createUser(createUserDTO);
        
        return Result.success(userId);
    }
    
    
    /**
     * 修改用户基础信息接口
     * @param id 用户ID
     * @param updateUserDTO 更新用户信息DTO
     * @return 无信息返回成功
     */
    @PutMapping("/users/{id}")
    public Result<Void> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserDTO updateUserDTO
    ) {
        userService.updateUser(id, updateUserDTO);
        
        return Result.success();
    }
    
    /**
     * 逻辑删除用户接口
     * @param id 用户ID
     * @return 返回统一结果
     */
    @DeleteMapping("/users/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
    
    /**
     * 修改用户启用状态接口
     * @param id 用户ID
     * @param updateUserStatusDTO 用户更新状态DTO
     * @return 统一返回结果
     */
    @PutMapping("/users/{id}/status")
    public Result<Void> updateUserStatus(@PathVariable Long id,
                                         @Valid @RequestBody UpdateUserStatusDTO updateUserStatusDTO
                                         ) {
        userService.updateUserStatus(id, updateUserStatusDTO);
        
        return Result.success();
    }
    
    /**
     * 查询用户详情接口
     * @param id 用户ID
     * @return 返回用户详情VO
     */
    @GetMapping("/users/{id}")
    public Result<UserDetailVO> getUserDetail(@PathVariable Long id) {
        UserDetailVO userDetailVO = userService.getUserDetail(id);
        
        return Result.success(userDetailVO);
    }
    
    
    /**
     * 修改用户角色接口
     */
    @OperationLogAnnotation(module = "用户管理", operation = "修改用户角色")
    @PutMapping("/{id}/roles")
    public Result<Void> updateUserRoles(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRolesDTO rolesDTO
            ) {
        userService.updateUserRoles(id, rolesDTO);
        return Result.success();
    }
    
    /**
     * 重置用户密码接口
     * @param id 用户ID
     */
    @PutMapping("/users/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return Result.success();
    }
}
