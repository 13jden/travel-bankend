package com.dzk.web.api.product.category;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzk.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 添加分类
     */
    @Transactional
    public CategoryDto add(CategoryDto.Input input) {
        try {
            Category category = CategoryConverter.toEntity(input);
            categoryMapper.insert(category);
            return CategoryConverter.toDto(category);
        } catch (Exception e) {
            throw new BusinessException("添加分类失败: " + e.getMessage());
        }
    }

    /**
     * 更新分类
     */
    @Transactional
    public CategoryDto update(Long id, CategoryDto.Input input) {
        try {
            Category existingCategory = categoryMapper.selectById(id);
            if (existingCategory == null) {
                throw new BusinessException("分类不存在！");
            }
            
            Category category = CategoryConverter.toEntity(input);
            category.setId(id);
            categoryMapper.updateById(category);
            
            return CategoryConverter.toDto(category);
        } catch (Exception e) {
            throw new BusinessException("更新分类失败: " + e.getMessage());
        }
    }

    /**
     * 获取分类详情
     */
    public CategoryDto getDetail(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        
        return CategoryConverter.toDto(category);
    }

    /**
     * 删除分类
     */
    @Transactional
    public CategoryDto delete(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        
        try {
            categoryMapper.deleteById(id);
            return CategoryConverter.toDto(category);
        } catch (Exception e) {
            throw new BusinessException("删除分类失败: " + e.getMessage());
        }
    }

    /**
     * 获取分类列表
     * @return
     */
    public List<CategoryDto> getList() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        
        return CategoryConverter.toDtoList(categories);
    }
}