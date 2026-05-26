package com.kory.fixflow.department.mapper;

import com.kory.fixflow.department.entity.Department;
import com.kory.fixflow.department.vo.DepartmentTreeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门数据访问层
 */
@Mapper
public interface DepartmentMapper {
    
    /**
     * 新增部门
     * @param department 部门实体
     * @return 受影响行数
     */
    int insertDepartment(Department department);
    
    /**
     * 查询所有未删除部门
     * @return 部门树 VO
     */
    List<DepartmentTreeVO> selectAllDepartments();
    
    /**
     * 根据ID查询部门
     * @param id 部门ID
     * @return 部门
     */
    Department selectById(Long id);
    
    
    /*
    修改部门
     */
    int updateDepartment(Department department);
    
    /*
    逻辑删除部门
     */
    int deleteDepartmentById(Long id);
    
    /*
    查询某部门下是否存在子部门
     */
    Long countChildrenByParentId(Long parentId);
    
    /*
    查询某部门下是否仍存在用户
     */
    Long countUsersByDepartmentId(Long departmentId);
    
    /**
     * 查询同一父部门下，是否存在同名部门
     * @param deptName 部门名称
     * @param parentId 上级部门ID
     * @param excludeId 排除自己
     * @return 个数
     */
    Long countSameNameDepartment(
            @Param("deptName") String deptName,
            @Param("parentId") Long parentId,
            @Param("excludeId") Long excludeId
    );
}
