package com.kory.fixflow.asset.service;

import com.kory.fixflow.asset.dto.AssetPageQueryDTO;
import com.kory.fixflow.asset.dto.CreateAssetDTO;
import com.kory.fixflow.asset.dto.UpdateAssetDTO;
import com.kory.fixflow.asset.vo.AssetDetailVO;
import com.kory.fixflow.asset.vo.AssetPageVO;
import com.kory.fixflow.common.page.PageResult;

/**
 * 资产业务层接口
 */
public interface AssetService {
    
    /**
     * 新增资产
     * @param createAssetDTO 新增资产DTO
     * @return 新增资产 ID
     */
    Long createAsset(CreateAssetDTO createAssetDTO);
    
    /**
     * 资产分页查询
     */
    PageResult<AssetPageVO> pageAssets(AssetPageQueryDTO queryDTO);
    
    
    /**
     * 查询资产详情
     */
    AssetDetailVO getAssetDetail(Long id);
    
    
    /**
     * 修改资产
     */
    void updateAsset(Long id, UpdateAssetDTO updateAssetDTO);
    
    
    /**
     * 删除资产
     */
    void deleteAsset(Long id);
}
