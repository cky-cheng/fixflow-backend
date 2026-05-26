package com.kory.fixflow.asset.controller;

import com.kory.fixflow.asset.dto.AssetPageQueryDTO;
import com.kory.fixflow.asset.dto.CreateAssetDTO;
import com.kory.fixflow.asset.dto.UpdateAssetDTO;
import com.kory.fixflow.asset.service.AssetService;
import com.kory.fixflow.asset.vo.AssetDetailVO;
import com.kory.fixflow.asset.vo.AssetPageVO;
import com.kory.fixflow.common.page.PageResult;
import com.kory.fixflow.common.result.Result;
import com.kory.fixflow.log.annotation.OperationLogAnnotation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 资产管理控制器
 */
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
@RequestMapping("/assets")
public class AssetController {
    
    private final AssetService assetService;
    
    /**
     * 新增资产
     * @param createAssetDTO 新增资产DTO
     * @return 新增的资产ID
     */
    @OperationLogAnnotation(module = "资产管理", operation = "新增资产")
    @PostMapping
    public Result<Long> createAsset(@Valid @RequestBody CreateAssetDTO createAssetDTO) {
        Long assetId = assetService.createAsset(createAssetDTO);
        return Result.success(assetId);
    }
    
    
    /**
     * 资产分页查询
     * @param queryDTO 资产分页DTO
     * @return 统一分页结果
     */
    @GetMapping("/page")
    public Result<PageResult<AssetPageVO>> pageAsset(@Valid AssetPageQueryDTO queryDTO) {
        PageResult<AssetPageVO> pageResult = assetService.pageAssets(queryDTO);
        return Result.success(pageResult);
    }
    
    
    /**
     * 查询资产详情
     * @param id 资产ID
     * @return 资产详情VO
     */
    @GetMapping("/{id}")
    public Result<AssetDetailVO> getAssetDetail(@PathVariable Long id) {
        AssetDetailVO assetDetailVO = assetService.getAssetDetail(id);
        return Result.success(assetDetailVO);
    }
    
    
    /**
     * 修改资产
     * @param id 资产ID
     * @param updateAssetDTO 更新资产DTO
     */
    @OperationLogAnnotation(module = "资产管理", operation = "修改资产")
    @PutMapping("/{id}")
    public Result<Void> updateAsset(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAssetDTO updateAssetDTO
            ) {
        assetService.updateAsset(id, updateAssetDTO);
        return Result.success();
    }
    
    
    /**
     * 删除资产
     * @param id 资产ID
     */
    @OperationLogAnnotation(module = "资产管理", operation = "删除资产")
    @DeleteMapping("/{id}")
    public Result<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return Result.success();
    }
}
