package com.kory.fixflow.assetcategory.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 新增资产分类 DTO
 */
@Data
public class CreateAssetCategoryDTO {
    /*
    分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 100, message = "分类名称不能超过100个字符")
    private String categoryName;
    
    /*
    父分类ID
     */
    @NotNull(message = "父分类ID不能为空")
    @Min(value = 0, message = "父分类ID不能小于0")
    private Long parentId;
    
    /*
    排序值
     */
    @NotNull(message = "排序值不能为空")
    @Min(value = 0, message = "排序值不能小于0")
    private Integer sortNum;
    
    /*
    状态
     */
    @NotNull(message = "分类状态不能为空")
    @Min(value = 0, message = "分类状态只能是0或1")
    @Max(value = 1, message = "分类状态只能是0或1")
    private Integer status;
    
    
}
