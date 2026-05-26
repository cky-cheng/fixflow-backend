package com.kory.fixflow.role.controller;

import com.kory.fixflow.common.result.Result;
import com.kory.fixflow.role.service.RoleService;
import com.kory.fixflow.role.vo.RoleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {
    
    private final RoleService roleService;
    
    /**
     * 查询所有启用角色
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Result<List<RoleVO>> listEnabledRoles() {
        List<RoleVO> roles = roleService.listEnabledRoles();
        return Result.success(roles);
    }
}
