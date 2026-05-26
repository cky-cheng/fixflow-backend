package com.kory.fixflow.assetcategory.controller;

import com.kory.fixflow.assetcategory.dto.CreateAssetCategoryDTO;
import com.kory.fixflow.assetcategory.dto.UpdateAssetCategoryDTO;
import com.kory.fixflow.assetcategory.service.AssetCategoryService;
import com.kory.fixflow.assetcategory.vo.AssetCategoryTreeVO;
import com.kory.fixflow.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产分类控制器
 */
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
@RequestMapping("/asset-categories")
public class AssetCategoryController {
    
    private final AssetCategoryService assetCategoryService;
    
    /**
     * 新增资产分类
     *
     * @param createAssetCategoryDTO DTO
     * @return id
     */
    @PostMapping
    public Result<Long> createCategory(@Valid @RequestBody CreateAssetCategoryDTO createAssetCategoryDTO) {
        Long categoryId = assetCategoryService.createAssetCategory(createAssetCategoryDTO);
        return Result.success(categoryId);
    }
    
    /**
     * 查询资产分类树
     *
     * @return List
     */
    @GetMapping("/tree")
    public Result<List<AssetCategoryTreeVO>> getCategoryTree() {
        List<AssetCategoryTreeVO> tree = assetCategoryService.getAssetCategoryTree();
        return Result.success(tree);
    }
    
    /**
     * 修改资产分类
     *
     * @param id 资产分类ID
     * @param updateAssetCategoryDTO DTO
     */
    @PutMapping("/{id}")
    public Result<Void> updateCategory(@PathVariable Long id, @Valid @RequestBody UpdateAssetCategoryDTO updateAssetCategoryDTO) {
        assetCategoryService.updateAssetCategory(id, updateAssetCategoryDTO);
        return Result.success();
    }
    
    
    /**
     * 逻辑删除资产分类
     * @param id 资产分类ID
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        assetCategoryService.deleteAssetCategory(id);
        return Result.success();
    }
    
}
