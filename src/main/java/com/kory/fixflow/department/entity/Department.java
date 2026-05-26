package com.kory.fixflow.department.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 部门实体类
 */
@Data
public class Department {
    private Long id;    // 部门ID
    private String deptName;    // 部门名称
    private Long parentId;  // 上级部门ID
    private Long leaderId;  // 部门负责人ID
    private Integer sortNum;    // 排序值
    private Integer status; // 状态
    private Integer isDeleted;  // 逻辑删除
    private LocalDateTime createTime;   // 创建时间
    private LocalDateTime updateTime;   // 更新时间
}
