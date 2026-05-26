package com.kory.fixflow.asset.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资产分页展示VO
 */
@Data
public class AssetPageVO {
    private Long id;    // 资产ID
    private String assetNo;     // 资产编号
    private String assetName;   // 资产名称
    private Long categoryId;    // 资产分类ID
    private String categoryName;    // 分类名称
    private Long departmentId;  // 所属部门ID
    private String departmentName;  // 所属部门名称
    private Long userId;        // 当前使用人ID
    private String userRealName;    // 当前使用人姓名
    private LocalDate purchaseDate; // 购入日期
    private String location;    // 存放位置/使用地点
    private Integer status;     // 资产状态
    private LocalDateTime createTime;   // 创建时间
}
