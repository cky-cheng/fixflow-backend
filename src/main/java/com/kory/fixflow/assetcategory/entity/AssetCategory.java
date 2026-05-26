package com.kory.fixflow.assetcategory.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 资产分类实体
 */
@Data
public class AssetCategory {
    private Long id;    // 分类ID
    private String categoryName;    // 分类名称
    private Long parentId;      // 父分类ID
    private Integer sortNum;    // 排序值
    private Integer status;     // 状态
    private Integer isDeleted;  // 逻辑删除
    private LocalDateTime createTime;   // 创建时间
    private LocalDateTime updateTime;   // 更新时间
}
