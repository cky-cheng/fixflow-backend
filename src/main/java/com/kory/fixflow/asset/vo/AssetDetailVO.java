package com.kory.fixflow.asset.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资产详情 VO
 * *
 * 用于：
 * GET /assets/{id}
 */
@Data
public class AssetDetailVO {
    
    /**
     * 资产ID
     */
    private Long id;
    
    /**
     * 资产编号
     */
    private String assetNo;
    
    /**
     * 资产名称
     */
    private String assetName;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 分类名称
     */
    private String categoryName;
    
    /**
     * 部门ID
     */
    private Long departmentId;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 使用人ID
     */
    private Long userId;
    
    /**
     * 使用人真实姓名
     */
    private String userRealName;
    
    /**
     * 购入日期
     */
    private LocalDate purchaseDate;
    
    /**
     * 存放位置
     */
    private String location;
    
    /**
     * 资产状态
     */
    private Integer status;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
}
