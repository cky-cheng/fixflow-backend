package com.kory.fixflow.department.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门树 VO
 * 用于返回树形结构：
 * 总公司
 * ├── 技术部
 * │   ├── 后端组
 * │   └── 前端组
 * └── 行政部
 */
@Data
public class DepartmentTreeVO {
    private Long id;    // 部门ID
    private String deptName;    // 部门名称
    private Long parentId;      // 上级部门ID
    private Long leaderId;      // 部门负责人ID
    private Integer sortNum;    // 排序值
    private Integer status;     // 状态
    
    /*
    子部门列表
    
    默认空集合
     */
    private List<DepartmentTreeVO> children = new ArrayList<>();
}
