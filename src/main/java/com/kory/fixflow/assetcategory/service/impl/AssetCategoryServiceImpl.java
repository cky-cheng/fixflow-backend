package com.kory.fixflow.assetcategory.service.impl;

import com.kory.fixflow.asset.mapper.AssetMapper;
import com.kory.fixflow.assetcategory.dto.CreateAssetCategoryDTO;
import com.kory.fixflow.assetcategory.dto.UpdateAssetCategoryDTO;
import com.kory.fixflow.assetcategory.entity.AssetCategory;
import com.kory.fixflow.assetcategory.mapper.AssetCategoryMapper;
import com.kory.fixflow.assetcategory.service.AssetCategoryService;
import com.kory.fixflow.assetcategory.vo.AssetCategoryTreeVO;
import com.kory.fixflow.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 资产分类业务层实现类
 */
@Service
@RequiredArgsConstructor
public class AssetCategoryServiceImpl implements AssetCategoryService {
    
    private final AssetCategoryMapper assetCategoryMapper;
    
    private final AssetMapper assetMapper;
    
    /**
     * 新增资产分类
     * @param createAssetCategoryDTO 新增资产分类DTO
     */
    @Override
    public Long createAssetCategory(CreateAssetCategoryDTO createAssetCategoryDTO) {
    
        //是否是顶级分类
        if (!Long.valueOf(0).equals(createAssetCategoryDTO.getParentId())) {
            AssetCategory parent = assetCategoryMapper.selectById(createAssetCategoryDTO.getParentId());
            
            if (parent == null ){
                throw new BusinessException("父分类不存在");
            }
        }
        
        // 同级分类名称不能重复
        Long sameNameCount = assetCategoryMapper.countSameNameCategory(
                createAssetCategoryDTO.getCategoryName(),
                createAssetCategoryDTO.getParentId(),
                null
                );
        
        if (sameNameCount != null && sameNameCount > 0) {
            throw new BusinessException("同级分类名称已经存在");
        }
        
        // DTO 转 Entity
        AssetCategory assetCategory = new AssetCategory();
        assetCategory.setCategoryName(createAssetCategoryDTO.getCategoryName());
        assetCategory.setParentId(createAssetCategoryDTO.getParentId());
        assetCategory.setSortNum(createAssetCategoryDTO.getSortNum());
        assetCategory.setStatus(createAssetCategoryDTO.getStatus());
        
        // 入库
        assetCategoryMapper.insertCategory(assetCategory);
        
        // 返回主键
        return assetCategory.getId();
    }
    
    /**
     * 查询资产分类树
     * @return 资产分类树
     */
    @Override
    public List<AssetCategoryTreeVO> getAssetCategoryTree() {
        
        // 查询所有未删除分类
        List<AssetCategoryTreeVO> allCategories = assetCategoryMapper.selectAllCategories();
        
        // 找出所有顶级分类
        List<AssetCategoryTreeVO> rootList = new ArrayList<>();
        
        for (AssetCategoryTreeVO assetCategoryTreeVO : allCategories) {
            if (Long.valueOf(0).equals(assetCategoryTreeVO.getParentId())) {
                rootList.add(assetCategoryTreeVO);
            }
        }
        
        // 递归挂载子分类
        for (AssetCategoryTreeVO root : rootList) {
            buildChildren(root, allCategories);
        }
        return rootList;
    }
    
    /**
     * 递归构建子分类树
     * @param parent 父分类
     * @param allCategories 所有分类
     */
    private void buildChildren(AssetCategoryTreeVO parent, List<AssetCategoryTreeVO> allCategories) {
        for (AssetCategoryTreeVO category : allCategories) {
            if (parent.getId().equals(category.getParentId())) {
                parent.getChildren().add(category);
                
                buildChildren(category, allCategories);
            }
        }
    }
    
    /**
     * 更新资产分类
     * @param id 资产分类ID
     * @param updateAssetCategoryDTO 更新资产分类DTO
     */
    @Override
    public void updateAssetCategory(Long id, UpdateAssetCategoryDTO updateAssetCategoryDTO) {
        
        // 当前分类必须存在
        AssetCategory assetCategory = assetCategoryMapper.selectById(id);
        
        if (assetCategory == null) {
            throw new BusinessException("资产分类不存在");
        }
        
        // 不能把自己设置成自己的父分类
        if (id.equals(updateAssetCategoryDTO.getParentId())) {
            throw new BusinessException("父分类不能是当前分类本身");
        }
        
        // 如果不是顶级分类，则父分类必须存在
        if (!Long.valueOf(0).equals(updateAssetCategoryDTO.getParentId())) {
            AssetCategory parent = assetCategoryMapper.selectById(updateAssetCategoryDTO.getParentId());
            
            if (parent == null) {
                throw new BusinessException("父分类不存在");
            }
            
            // 不能把自己的下级分类设置成新的父分类
            List<AssetCategoryTreeVO> allCategories = assetCategoryMapper.selectAllCategories();
            
            boolean targetParentIsDescendant = isDescendant(id, updateAssetCategoryDTO.getParentId(), allCategories);
            
            if (targetParentIsDescendant) {
                throw new BusinessException("父分类不能是当前分类的下级分类");
            }
        }
        
        // 校验同级重名，修改时排除自己
        Long sameNameCount = assetCategoryMapper.countSameNameCategory(
                updateAssetCategoryDTO.getCategoryName(),
                updateAssetCategoryDTO.getParentId(),
                id
        );
        
        if (sameNameCount != null && sameNameCount > 0) {
            throw new BusinessException("同级分类名称已经存在");
        }
        
        // 更新字段
        assetCategory.setCategoryName(updateAssetCategoryDTO.getCategoryName());
        assetCategory.setParentId(updateAssetCategoryDTO.getParentId());
        assetCategory.setSortNum(updateAssetCategoryDTO.getSortNum());
        assetCategory.setStatus(updateAssetCategoryDTO.getStatus());
        
        // 更新
        assetCategoryMapper.updateCategory(assetCategory);
    }
    
    /**
     * 判断 targetId 是否是 currentId 的下级分类
     */
    private boolean isDescendant(
            Long currentId,
            Long targetId,
            List<AssetCategoryTreeVO> allCategories
    ) {
        for(AssetCategoryTreeVO category : allCategories) {
            
            // 找出 currentId 的直接子分类
            if (currentId.equals(category.getParentId())) {
                
                // 如果目标父分类刚好是某个子分类  返回 true
                if (targetId.equals(category.getId())) {
                    return true;
                }
                
                // 继续递归查找更深层次的子分类
                boolean found = isDescendant(category.getId(), targetId, allCategories);
                
                if (found) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 删除资产分类
     * @param id 资产分类ID
     */
    @Override
    public void deleteAssetCategory(Long id) {
        
        // 分类必须存在
        AssetCategory assetCategory = assetCategoryMapper.selectById(id);
        
        if (assetCategory == null) {
            throw new BusinessException("资产分类不存在");
        }
        
        // 仍有子分类，不允许删除
        Long childCount = assetCategoryMapper.countChildrenByParentId(id);
        
        if (childCount != null && childCount > 0) {
            throw new BusinessException("该分类下仍存在子分类，无法删除");
        }
        
        // 分类下仍存在资产，不允许删除
        Long assetCount = assetMapper.countAssetsByCategoryId(id);
        
        if (assetCount != null && assetCount > 0) {
            throw new BusinessException("该分类下仍存在资产，无法删除");
        }
        
        
        // 执行逻辑删除
        assetCategoryMapper.deleteCategoryById(id);
    }
}
