package com.kory.fixflow.asset.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 资产分页查询DTO
 */
@Data
public class AssetPageQueryDTO {
    
    /*
    当前页码
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;// 默认

    /*
    每页条数
     */
    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer pageSize = 5;
    
    /*
    资产编号，模糊查询
     */
    private String assetNo;
    
    /*
    资产名称，模糊查询
     */
    private String assetName;
    
    /*
    分类ID，精确筛选
     */
    private Long categoryId;
    
    /*
    部门ID，精确筛选
     */
    private Long departmentId;
    
    /*
    资产状态，精确筛选
     */
    private Integer status;
    
    /*
    MySQL 分页偏移量
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
