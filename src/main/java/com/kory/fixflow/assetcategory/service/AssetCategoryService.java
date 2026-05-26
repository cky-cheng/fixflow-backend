package com.kory.fixflow.assetcategory.service;

import com.kory.fixflow.assetcategory.dto.CreateAssetCategoryDTO;
import com.kory.fixflow.assetcategory.dto.UpdateAssetCategoryDTO;
import com.kory.fixflow.assetcategory.vo.AssetCategoryTreeVO;

import java.util.List;

/**
 * 资产分类业务层接口
 */
public interface AssetCategoryService {
    /**
     * 新增资产分类
     * @param createAssetCategoryDTO 新增资产分类DTO
     */
    Long createAssetCategory(CreateAssetCategoryDTO createAssetCategoryDTO);
    
    /**
     * 查询资产分类树
     * @return 资产分类树
     */
    List<AssetCategoryTreeVO> getAssetCategoryTree();
    
    
    /**
     * 更新资产分类
     * @param id 资产分类ID
     * @param updateAssetCategoryDTO 更新资产分类DTO
     */
    void updateAssetCategory(Long id, UpdateAssetCategoryDTO updateAssetCategoryDTO);
    
    /**
     * 删除资产分类
     * @param id 资产分类ID
     */
    void deleteAssetCategory(Long id);
}
