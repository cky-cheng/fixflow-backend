package com.kory.fixflow.role.service.impl;

import com.kory.fixflow.role.mapper.RoleMapper;
import com.kory.fixflow.role.service.RoleService;
import com.kory.fixflow.role.vo.RoleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色业务层实现类
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    /**
     * 查询所有启用角色
     */
    @Override
    public List<RoleVO> listEnabledRoles() {
        return roleMapper.selectEnableRoles();
    }
}
