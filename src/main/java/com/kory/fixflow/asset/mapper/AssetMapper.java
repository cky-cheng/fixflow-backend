package com.kory.fixflow.asset.mapper;

import com.kory.fixflow.asset.dto.AssetPageQueryDTO;
import com.kory.fixflow.asset.entity.Asset;
import com.kory.fixflow.asset.vo.AssetDetailVO;
import com.kory.fixflow.asset.vo.AssetPageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 资产数据访问层
 */
@Mapper
public interface AssetMapper {
    
    /*
     * 根据资产编号查询资产
     *
     *  新增时判断编号是否重复
     */
    Asset selectByAssetNo(String assetNo);
    
    
    /**
     * 新增资产
     * @param asset 资产实体
     * @return 受影响行数
     */
    int insertAsset(Asset asset);
    
    
    /**
     * 资产分页列表查询
     * @param queryDTO 资产分页查询DTO
     * @return 分页列表
     */
    List<AssetPageVO> selectAssetPageList(AssetPageQueryDTO queryDTO);
    
    /**
     * 资产分页总数查询
     * @param queryDTO 资产分页查询DTO
     * @return 分页总数
     */
    Long selectAssetPageCount(AssetPageQueryDTO queryDTO);
    
    /**
     * 根据ID查询资产实体   修改前/删除前
     * @param id 资产ID
     * @return 资产实体
     */
    Asset selectById(Long id);
    
    /**
     * 查询资产详情
     * @param id 资产ID
     * @return 资产详情VO
     */
    AssetDetailVO selectAssetDetailById(Long id);
    
    
    /**
     * 修改资产
     * @param asset 资产实体
     */
    int updateAsset(Asset asset);
    
    /**
     * 逻辑删除资产
     * @param id 资产ID
     */
    int deleteAssetById(Long id);
    
    /**
     * 查询某个分类下的资产数量
     * @param categoryId 分类ID
     * @return 资产数量
     */
    Long countAssetsByCategoryId(Long categoryId);
    
    /**
     * 查询某个部门下的资产数量
     * @param departmentId 部门ID
     * @return 资产数量
     */
    Long countAssetsByDepartmentId(Long departmentId);
}
