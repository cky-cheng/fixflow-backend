package com.kory.fixflow.department.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 修改部门DTO
 */
@Data
public class UpdateDepartmentDTO {
    
    // 部门名称
    @NotBlank(message = "部门名称不能为空")
    @Size(max = 100, message = "部门名称不能超过100个字符")
    private String deptName;
    
    // 上级部门ID
    @NotNull(message = "上级部门ID不能为空")
    @Min(value = 0, message = "上级部门ID不能小于0")
    private Long parentId;
    
    // 部门负责人ID      暂时可以为空
    private Long leaderId;
    
    // 排序值
    @NotNull(message = "排序值不能为空")
    @Min(value = 0, message = "排序值不能小于0")
    private Integer sortNum;
    
    // 部门状态
    @NotNull(message = "部门状态不能为空")
    @Min(value = 0, message = "部门状态只能是0或1")
    @Max(value = 1, message = "部门状态只能是0或1")
    private Integer status;
}
