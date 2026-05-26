package com.kory.fixflow.asset.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资产实体类
 */
@Data
public class Asset {
    private Long id;    // 资产ID
    private String assetNo;     // 资产编号
    private String assetName;   // 资产名称
    private Long categoryId;    // 资产分类ID
    private Long departmentId;  // 所属部门ID
    private Long userId;        // 当前使用人ID
    private LocalDate purchaseDate; // 购入日期
    private String location;    // 存放位置/使用地点
    private Integer status;     // 资产状态
    private String remark;      // 备注
    private Integer isDeleted;  // 逻辑删除
    private LocalDateTime createTime;   // 创建时间
    private LocalDateTime updateTime;   // 更新时间
}
