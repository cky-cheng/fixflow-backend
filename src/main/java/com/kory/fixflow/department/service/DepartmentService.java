package com.kory.fixflow.department.service;

import com.kory.fixflow.department.dto.CreateDepartmentDTO;
import com.kory.fixflow.department.dto.UpdateDepartmentDTO;
import com.kory.fixflow.department.vo.DepartmentTreeVO;

import java.util.List;

/**
 * 部门业务层接口
 */
public interface DepartmentService {
    
    /*
    新增部门
     */
    Long createDepartment(CreateDepartmentDTO createDepartmentDTO);
    
    /*
    查询部门树
     */
    List<DepartmentTreeVO> getDepartmentTree();
    
    /*
    修改部门
     */
    void updateDepartment(Long id, UpdateDepartmentDTO updateDepartmentDTO);
    
    /*
    删除部门
     */
    void deleteDepartment(Long id);
}
