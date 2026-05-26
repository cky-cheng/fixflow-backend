package com.kory.fixflow.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 修改用户角色 DTO
 */
@Data
public class UpdateUserRolesDTO {
    
    /*
    角色ID 列表
     */
    @NotEmpty(message = "用户角色不能为空")
    private List<Long> roleIds;
}
