package com.kory.fixflow.asset.service.impl;

import com.kory.fixflow.asset.dto.AssetPageQueryDTO;
import com.kory.fixflow.asset.dto.CreateAssetDTO;
import com.kory.fixflow.asset.dto.UpdateAssetDTO;
import com.kory.fixflow.asset.entity.Asset;
import com.kory.fixflow.asset.mapper.AssetMapper;
import com.kory.fixflow.asset.service.AssetService;
import com.kory.fixflow.asset.vo.AssetDetailVO;
import com.kory.fixflow.asset.vo.AssetPageVO;
import com.kory.fixflow.assetcategory.entity.AssetCategory;
import com.kory.fixflow.assetcategory.mapper.AssetCategoryMapper;
import com.kory.fixflow.common.exception.BusinessException;
import com.kory.fixflow.common.page.PageResult;
import com.kory.fixflow.department.entity.Department;
import com.kory.fixflow.department.mapper.DepartmentMapper;
import com.kory.fixflow.user.entity.User;
import com.kory.fixflow.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资产业务层实现类
 */
@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {
    
    // 资产 Mapper
    private final AssetMapper assetMapper;
    
    // 分类 Mapper
    private final AssetCategoryMapper assetCategoryMapper;
    
    // 部门 Mapper
    private final DepartmentMapper departmentMapper;
    
    // 用户 Mapper
    private final UserMapper userMapper;
    
    
    /**
     * 新增资产
     * @param createAssetDTO 新增资产DTO
     * @return 新增资产的ID
     */
    @Override
    public Long createAsset(CreateAssetDTO createAssetDTO) {
        /*
        校验资产编号是否存在
         */
        Asset existAsset = assetMapper.selectByAssetNo(createAssetDTO.getAssetNo());
        
        if (existAsset != null) {
            throw new BusinessException("资产编号已经存在");
        }
        
        /*
        校验资产分类是否存在
         */
        AssetCategory category = assetCategoryMapper.selectById(createAssetDTO.getCategoryId());
        
        if (category == null) {
            throw new BusinessException("资产分类不存在");
        }
        
        /*
        已禁用分类不允许继续挂新资产
         */
        if (Integer.valueOf(0).equals(category.getStatus())) {
            throw new BusinessException("资产分类已禁用");
        }
        
        /*
        校验所属部门是否存在
         */
        Department department = departmentMapper.selectById(createAssetDTO.getDepartmentId());
        
        if (department == null) {
            throw new BusinessException("所属部门不存在");
        }
        
        /*
        已禁用部门不允许继续新增归属资产
         */
        if (Integer.valueOf(0).equals(department.getStatus())) {
            throw new BusinessException("所属部门已禁用");
        }
        
        /*
        如果传入使用人ID，使用人必须存在
         */
        if (createAssetDTO.getUserId() != null) {
            
            User user = userMapper.selectById(createAssetDTO.getUserId());
            
            if (user == null) {
                throw new BusinessException("使用人不存在");
            }
            
            if (Integer.valueOf(0).equals(user.getStatus())) {
                throw new BusinessException("使用人账号已经被禁用");
            }
        }
        
        
        /*
        DTO 转 Entity
         */
        Asset asset = new Asset();
        asset.setAssetNo(createAssetDTO.getAssetNo());
        asset.setAssetName(createAssetDTO.getAssetName());
        asset.setCategoryId(createAssetDTO.getCategoryId());
        asset.setDepartmentId(createAssetDTO.getDepartmentId());
        asset.setUserId(createAssetDTO.getUserId());
        asset.setPurchaseDate(createAssetDTO.getPurchaseDate());
        asset.setLocation(createAssetDTO.getLocation());
        asset.setStatus(createAssetDTO.getStatus());
        asset.setRemark(createAssetDTO.getRemark());
        
        /*
        入库
         */
        assetMapper.insertAsset(asset);
        
        /*
        返回资产ID
         */
        return asset.getId();
    }
    
    
    /**
     * 资产分页查询
     * @param queryDTO 资产分页查询DTO
     * @return 分页结果
     */
    @Override
    public PageResult<AssetPageVO> pageAssets(AssetPageQueryDTO queryDTO) {
        
        /*
        查询当前页列表
         */
        List<AssetPageVO> records = assetMapper.selectAssetPageList(queryDTO);
        
        /*
        查询总数
         */
        Long total = assetMapper.selectAssetPageCount(queryDTO);
        
        // 封装统一分页返回结果
        return PageResult.of(
                records,
                total,
                queryDTO.getPageNum(),
                queryDTO.getPageSize()
        );
    }
    
    /**
     * 查询资产详情
     * @param id 资产ID
     * @return 资产详情VO
     */
    @Override
    public AssetDetailVO getAssetDetail(Long id) {
        
        // 查询资产详情
        AssetDetailVO detailVO = assetMapper.selectAssetDetailById(id);
        
        if (detailVO == null) {
            throw new BusinessException("资产不存在");
        }
        
        return detailVO;
    }
    
    
    /**
     * 修改资产
     * @param id 资产ID
     * @param updateAssetDTO 更新资产DTO
     */
    @Override
    public void updateAsset(Long id, UpdateAssetDTO updateAssetDTO) {
        
        // 当前资产必须存在
        Asset asset = assetMapper.selectById(id);
        
        if (asset == null) {
            throw new BusinessException("资产不存在");
        }
        
        // 校验资产编号是否被其他资源占用
        Asset assetNoOwner = assetMapper.selectByAssetNo(updateAssetDTO.getAssetNo());
        
        if (assetNoOwner != null && !assetNoOwner.getId().equals(id)) {
            throw new BusinessException("资产编号已存在");
        }
        
        // 校验资产分类
        AssetCategory category = assetCategoryMapper.selectById(updateAssetDTO.getCategoryId());
        
        if (category == null) {
            throw new BusinessException("资产分类不存在");
        }
        
        if (Integer.valueOf(0).equals(category.getStatus())) {
            throw new BusinessException("资产分类已禁用");
        }
        
        // 校验所属部门
        Department department = departmentMapper.selectById(updateAssetDTO.getDepartmentId());
        
        if (department == null) {
            throw new BusinessException("所属部门不存在");
        }
        
        if (Integer.valueOf(0).equals(department.getStatus())) {
            throw new BusinessException("所属部门已禁用");
        }
        
        // 如设置了使用人，校验使用人
        if (updateAssetDTO.getUserId() != null) {
            
            User user = userMapper.selectById(updateAssetDTO.getUserId());
            
            if (user == null) {
                throw new BusinessException("使用人不存在");
            }
            
            if (Integer.valueOf(0).equals(user.getStatus())) {
                throw new BusinessException("使用人账号已被禁用");
            }
        }
        
        // 更新字段
        asset.setAssetNo(updateAssetDTO.getAssetNo());
        asset.setAssetName(updateAssetDTO.getAssetName());
        asset.setCategoryId(updateAssetDTO.getCategoryId());
        asset.setDepartmentId(updateAssetDTO.getDepartmentId());
        asset.setUserId(updateAssetDTO.getUserId());
        asset.setPurchaseDate(updateAssetDTO.getPurchaseDate());
        asset.setLocation(updateAssetDTO.getLocation());
        asset.setStatus(updateAssetDTO.getStatus());
        asset.setRemark(updateAssetDTO.getRemark());
        
        // 执行更新
        assetMapper.updateAsset(asset);
    }
    
    
    /**
     * 删除资产
     * @param id 资产ID
     */
    @Override
    public void deleteAsset(Long id) {
        
        // 资产必须存在
        Asset asset = assetMapper.selectById(id);
        
        if (asset == null) {
            throw new BusinessException("资产不存在");
        }
        
        // 执行逻辑删除
        assetMapper.deleteAssetById(id);
    }
}
