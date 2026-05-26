package com.kory.fixflow.assetcategory.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 资产分类树VO
 */
@Data
public class AssetCategoryTreeVO {
    private Long id;
    private String categoryName;
    private Long parentId;
    private Integer sortNum;
    private Integer status;
    
    // 子分类列表
    private List<AssetCategoryTreeVO> children = new ArrayList<>();
}
