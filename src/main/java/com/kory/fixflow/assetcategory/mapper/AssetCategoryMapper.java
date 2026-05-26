package com.kory.fixflow.assetcategory.mapper;

import com.kory.fixflow.assetcategory.entity.AssetCategory;
import com.kory.fixflow.assetcategory.vo.AssetCategoryTreeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资产分类数据访问层
 */
@Mapper
public interface AssetCategoryMapper {
    /**
     * 新增分类
     * @param assetCategory 资产分类实体
     */
    int insertCategory(AssetCategory assetCategory);
    
    /**
     * 查找全部未删除分类
     * @return 资产分类树VO
     */
    List<AssetCategoryTreeVO> selectAllCategories();
    
    /**
     * 通过ID查询分类
     * @param id 资产分类ID
     * @return 资产分类实体
     */
    AssetCategory selectById(Long id);
    
    /**
     * 更新分类
     * @param assetCategory 资产分类实体
     */
    int updateCategory(AssetCategory assetCategory);
    
    /**
     * 通过ID逻辑删除分类
     * @param id 资产分类ID
     */
    int deleteCategoryById(Long id);
    
    /**
     * 查询当前分类下子分类的数量
     * @param parentId 父分类ID
     */
    Long countChildrenByParentId(Long parentId);
    
    
    /**
     * 查询同级分类是否重名
     * @param categoryName 分类名称
     * @param parentId 父分类ID
     * @param excludeId 排除自己的ID
     */
    Long countSameNameCategory(
            @Param("categoryName") String categoryName,
            @Param("parentId") Long parentId,
            @Param("excludeId") Long excludeId
    );
}

