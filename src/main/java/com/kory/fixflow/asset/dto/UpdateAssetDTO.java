package com.kory.fixflow.asset.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * 修改资产 DTO
 * *
 * 当前允许修改：
 * 1. 资产编号
 * 2. 资产名称
 * 3. 分类
 * 4. 所属部门
 * 5. 使用人
 * 6. 购入日期
 * 7. 存放位置
 * 8. 状态
 * 9. 备注
 */
@Data
public class UpdateAssetDTO {
    /**
     * 资产编号
     */
    @NotBlank(message = "资产编号不能为空")
    @Size(max = 50, message = "资产编号不能超过50个字符")
    private String assetNo;
    
    /**
     * 资产名称
     */
    @NotBlank(message = "资产名称不能为空")
    @Size(max = 100, message = "资产名称不能超过100个字符")
    private String assetName;
    
    /**
     * 资产分类ID
     */
    @NotNull(message = "资产分类不能为空")
    private Long categoryId;
    
    /**
     * 所属部门ID
     */
    @NotNull(message = "所属部门不能为空")
    private Long departmentId;
    
    /**
     * 使用人ID，可为空
     */
    private Long userId;
    
    /**
     * 购入日期
     */
    private LocalDate purchaseDate;
    
    /**
     * 存放位置
     */
    @Size(max = 200, message = "存放位置不能超过200个字符")
    private String location;
    
    /**
     * 状态：
     * 1 使用中
     * 2 闲置
     * 3 维修中
     * 4 已报废
     */
    @NotNull(message = "资产状态不能为空")
    @Min(value = 1, message = "资产状态值不合法")
    @Max(value = 4, message = "资产状态值不合法")
    private Integer status;
    
    /**
     * 备注
     */
    @Size(max = 500, message = "备注不能超过500个字符")
    private String remark;
}
