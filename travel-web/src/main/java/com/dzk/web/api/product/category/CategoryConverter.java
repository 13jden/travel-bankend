package com.dzk.web.api.product.category;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryConverter {

    /**
     * 实体转DTO
     */
    public static CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .nameEn(category.getNameEn())
                .sort(category.getSort())
                .isEnable(category.getIsEnable())
                .build();
    }
    
    /**
     * 实体列表转DTO列表
     */
    public static List<CategoryDto> toDtoList(List<Category> categories) {
        if (categories == null) {
            return null;
        }
        
        return categories.stream()
                .map(CategoryConverter::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 输入DTO转实体
     */
    public static Category toEntity(CategoryDto.Input input) {
        if (input == null) {
            return null;
        }
        
        return Category.builder()
                .name(input.getName())
                .nameEn(input.getNameEn())
                .sort(input.getSort())
                .isEnable(input.getIsEnable())
                .build();
    }
} 