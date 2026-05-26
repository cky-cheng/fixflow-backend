package com.kory.fixflow.asset.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * 新增资产 DTO
 */
@Data
public class CreateAssetDTO {
    
    /*
    资产编号
     */
    @NotBlank(message = "资产编号不能为空")
    @Size(max = 50, message = "资产编号不能超过50个字符")
    private String assetNo;
    
    /*
    资产名称
     */
    @NotBlank(message = "资产名称不能为空")
    @Size(max = 100, message = "资产名称不能超过100个字符")
    private String assetName;
    
    /*
    资产分类 ID
     */
    @NotNull(message = "资产分类不能为空")
    private Long categoryId;
    
    /*
    所属部门ID
     */
    @NotNull(message = "所属部门不能为空")
    private Long departmentId;
    
    /*
    使用人ID
     */
    private Long userId;
    
    /*
    购入日期
     */
    private LocalDate purchaseDate;
    
    /*
    存储位置 / 使用地点
     */
    @Size(max = 200, message = "存放位置不能超过200个字符")
    private String location;
    
    /*
    资产状态    1使用中，2闲置，3维修中，4已报废
     */
    @NotNull(message = "资产状态不能为空")
    @Min(value = 1, message = "资产状态值不合法")
    @Max(value = 4, message = "资产状态值不合法")
    private Integer status;
    
    /*
    备注
     */
    @Size(max = 500, message = "备注不能超过500个字符")
    private String remark;
}
