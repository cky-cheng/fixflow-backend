package com.kory.fixflow.department.service.impl;

import com.kory.fixflow.asset.mapper.AssetMapper;
import com.kory.fixflow.common.exception.BusinessException;
import com.kory.fixflow.department.dto.CreateDepartmentDTO;
import com.kory.fixflow.department.dto.UpdateDepartmentDTO;
import com.kory.fixflow.department.entity.Department;
import com.kory.fixflow.department.mapper.DepartmentMapper;
import com.kory.fixflow.department.service.DepartmentService;
import com.kory.fixflow.department.vo.DepartmentTreeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 部门业务层实现类
 */
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    
    private final DepartmentMapper departmentMapper;
    
    private final AssetMapper assetMapper;
    
    /**
     * 新增部门
     * @param createDepartmentDTO 新增部门 DTO
     * @return 修改行数
     */
    @Override
    public Long createDepartment(CreateDepartmentDTO createDepartmentDTO) {
        // 如果 parentId 不为 0     父部门一定存在
        if (!Long.valueOf(0).equals(createDepartmentDTO.getParentId())) {
            Department parent = departmentMapper.selectById(createDepartmentDTO.getParentId());
            
            if (parent == null) {
                throw new BusinessException("上级部门不存在");
            }
        }
        
        /*
        校验同级部门是否重名
         */
        Long sameNameCount = departmentMapper.countSameNameDepartment(
                createDepartmentDTO.getDeptName(), createDepartmentDTO.getParentId(), null
        );
        
        if (sameNameCount != null && sameNameCount > 0) {
            throw new BusinessException("同级部门名称已经存在");
        }
        
        /*
        DTO 转 Entity
         */
        Department department = new Department();
        department.setDeptName(createDepartmentDTO.getDeptName());
        department.setParentId(createDepartmentDTO.getParentId());
        department.setLeaderId(createDepartmentDTO.getLeaderId());
        department.setSortNum(createDepartmentDTO.getSortNum());
        department.setStatus(createDepartmentDTO.getStatus());
        
        // 入库
        departmentMapper.insertDepartment(department);
        
        // 返回新部门ID
        return department.getId();
    }
    
    
    /**
     * 查询部门树
     * @return 部门树
     */
    @Override
    public List<DepartmentTreeVO> getDepartmentTree() {
        /*
        查出全部未被删除部门
         */
        List<DepartmentTreeVO> allDepartments = departmentMapper.selectAllDepartments();
        
        /*
        准备顶级部门列表
         */
        List<DepartmentTreeVO> rootList = new ArrayList<>();
        
        /*
        遍历所有部门
         */
        for (DepartmentTreeVO department : allDepartments) {
            if (Long.valueOf(0).equals(department.getParentId())) {
                rootList.add(department);
            }
        }
        
        /*
        为每个根节点递归组装子部门
         */
        for (DepartmentTreeVO root : rootList) {
            buildChildren(root, allDepartments);
        }
        
        return rootList;
    }
    
    /**
     * 递归组装子部门
     * @param parent 父部门
     * @param allDepartments 所有部门
     */
    private void buildChildren(DepartmentTreeVO parent, List<DepartmentTreeVO> allDepartments) {
        for (DepartmentTreeVO department : allDepartments) {
            /*
            如果某部门的 parentId 等于当前部门 id
                说明它是当前部门的子部门
             */
            if (parent.getId().equals(department.getParentId())) {
                parent.getChildren().add(department);

                /*
                继续递归找子部门的子部门
                 */
                buildChildren(department, allDepartments);
            }
        }
    }

    /**
     * 获取某个部门的所有子孙节点 ID（用于循环引用检查）
     * @param departmentId 部门 ID
     * @return 子孙节点 ID 集合
     */
    private Set<Long> getDescendantIds(Long departmentId) {
        List<DepartmentTreeVO> allDepartments = departmentMapper.selectAllDepartments();
        Set<Long> descendantIds = new HashSet<>();
        collectDescendantIds(departmentId, allDepartments, descendantIds);
        return descendantIds;
    }

    /**
     * 递归收集子孙节点 ID
     * @param parentId 父部门 ID
     * @param allDepartments 所有部门
     * @param descendantIds 子孙节点 ID 集合
     */
    private void collectDescendantIds(Long parentId, List<DepartmentTreeVO> allDepartments, Set<Long> descendantIds) {
        for (DepartmentTreeVO department : allDepartments) {
            if (parentId.equals(department.getParentId())) {
                descendantIds.add(department.getId());
                collectDescendantIds(department.getId(), allDepartments, descendantIds);
            }
        }
    }


    /**
     * 修改部门
     * @param id 部门id
     * @param updateDepartmentDTO 修改部门 DTO
     */
    @Override
    public void updateDepartment(Long id, UpdateDepartmentDTO updateDepartmentDTO) {
        
        // 当前部门必须存在
        Department department = departmentMapper.selectById(id);
        
        if (department == null) {
            throw new BusinessException("部门不存在");
        }
        
        // 不允许把自己设置成自己的父部门
        if (id.equals(updateDepartmentDTO.getParentId())) {
            throw new BusinessException("上级部门不能是当前部门本身");
        }

        // 不允许将子孙节点设置为父部门，防止循环引用
        if (!Long.valueOf(0).equals(updateDepartmentDTO.getParentId())) {
            Set<Long> descendantIds = getDescendantIds(id);
            if (descendantIds.contains(updateDepartmentDTO.getParentId())) {
                throw new BusinessException("上级部门不能是当前部门的下级部门");
            }
        }
        
        /*
        如果不是顶级部门，父部门一定存在
         */
        if (!Long.valueOf(0).equals(updateDepartmentDTO.getParentId())) {
            Department parent = departmentMapper.selectById(updateDepartmentDTO.getParentId());
            
            if (parent == null) {
                throw new BusinessException("上级部门不存在");
            }
        }
        
        /*
        校验同级部门名称是否重复，修改时要排除自己
         */
        Long sameNameCount = departmentMapper.countSameNameDepartment(
                updateDepartmentDTO.getDeptName(), updateDepartmentDTO.getParentId(), id
        );
        
        if (sameNameCount != null && sameNameCount > 0) {
            throw new BusinessException("同级部门已经存在");
        }
        
        /*
        更新字段
         */
        department.setDeptName(updateDepartmentDTO.getDeptName());
        department.setParentId(updateDepartmentDTO.getParentId());
        department.setLeaderId(updateDepartmentDTO.getLeaderId());
        department.setSortNum(updateDepartmentDTO.getSortNum());
        department.setStatus(updateDepartmentDTO.getStatus());
        
        /*
        执行更新
         */
        departmentMapper.updateDepartment(department);
    }
    
    
    /**
     * 删除部门
     * @param id 部门ID
     */
    @Override
    public void deleteDepartment(Long id) {
        
        /*
        部门必须存在
         */
        Department department = departmentMapper.selectById(id);
        
        if (department == null) {
            throw new BusinessException("部门不存在");
        }
        
        /*
        下面还有子部门，不允许删除
         */
        Long childCount = departmentMapper.countChildrenByParentId(id);
        
        if (childCount != null && childCount > 0) {
            throw new BusinessException("该部门仍存在子部门，无法删除");
        }
        
        /*
        部门下还有用户，不允许删除
         */
        Long userCount = departmentMapper.countUsersByDepartmentId(id);
        
        if (userCount != null && userCount > 0) {
            throw new BusinessException("该部门下仍存在用户，无法删除");
        }
        
        /*
        部门下仍存在资产，不允许删除
         */
        Long assetCount = assetMapper.countAssetsByDepartmentId(id);
        
        if (assetCount != null && assetCount > 0) {
            throw new BusinessException("该部门下仍存在资产，无法删除");
        }
        
        /*
        逻辑删除
         */
        departmentMapper.deleteDepartmentById(id);
    
    }
}
