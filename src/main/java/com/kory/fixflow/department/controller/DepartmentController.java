package com.kory.fixflow.department.controller;

import com.kory.fixflow.common.result.Result;
import com.kory.fixflow.department.dto.CreateDepartmentDTO;
import com.kory.fixflow.department.dto.UpdateDepartmentDTO;
import com.kory.fixflow.department.service.DepartmentService;
import com.kory.fixflow.department.vo.DepartmentTreeVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 */
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {
    
    private final DepartmentService departmentService;
    
    /**
     * 新增部门
     * @param createDepartmentDTO 新增部门 DTO
     */
    @PostMapping
    public Result<Long> createDepartment(@Valid @RequestBody CreateDepartmentDTO createDepartmentDTO) {
        Long departmentId = departmentService.createDepartment(createDepartmentDTO);
        return Result.success(departmentId);
    }
    
    /**
     * 查询部门树
     * @return 部门树
     */
    @GetMapping("/tree")
    public Result<List<DepartmentTreeVO>> getDepartmentTree() {
        List<DepartmentTreeVO> tree = departmentService.getDepartmentTree();
        return Result.success(tree);
    }
    
    
    /**
     * 修改部门
     * @param id 部门ID
     * @param updateDepartmentDTO 更新部门DTO
     */
    @PutMapping("/{id}")
    public Result<Void> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDepartmentDTO updateDepartmentDTO
            ) {
        departmentService.updateDepartment(id, updateDepartmentDTO);
        return Result.success();
    }
    
    /**
     * 删除部门
     * @param id 部门ID
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return Result.success();
    }
}
