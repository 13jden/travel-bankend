package com.dzk.web.api.product;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductConverter {

    /**
     * 实体转DTO
     */
    public static ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }
        
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .nameEn(product.getNameEn())
                .description(product.getDescription())
                .descriptionEn(product.getDescriptionEn())
                .price(product.getPrice())
                .stock(product.getStock())
                .categoryId(product.getCategoryId())
                .status(product.getStatus())
                .sales(product.getSales())
                .isRecommend(product.getIsRecommend())
                .isHot(product.getIsHot())
                .coverImageId(product.getCoverImageId())
                .build();
    }
    
    /**
     * 实体转详情DTO
     */
    public static ProductDto.Detail toDetailDto(Product product) {
        if (product == null) {
            return null;
        }
        
        ProductDto.Detail detail = new ProductDto.Detail();
        detail.setId(product.getId());
        detail.setName(product.getName());
        detail.setNameEn(product.getNameEn());
        detail.setDescription(product.getDescription());
        detail.setDescriptionEn(product.getDescriptionEn());
        detail.setPrice(product.getPrice());
        detail.setStock(product.getStock());
        detail.setCategoryId(product.getCategoryId());
        detail.setStatus(product.getStatus());
        detail.setSales(product.getSales());
        detail.setIsRecommend(product.getIsRecommend());
        detail.setIsHot(product.getIsHot());
        detail.setCoverImageId(product.getCoverImageId());
        
        return detail;
    }
    
    /**
     * 实体列表转DTO列表
     */
    public static List<ProductDto> toDtoList(List<Product> products) {
        if (products == null) {
            return null;
        }
        
        return products.stream()
                .map(ProductConverter::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 输入DTO转实体
     */
    public static Product toEntity(ProductDto.Input input) {
        if (input == null) {
            return null;
        }
        
        return Product.builder()
                .name(input.getName())
                .nameEn(input.getNameEn())
                .description(input.getDescription())
                .descriptionEn(input.getDescriptionEn())
                .price(input.getPrice())
                .stock(input.getStock())
                .categoryId(input.getCategoryId())
                .status(input.getStatus())
                .isRecommend(input.getIsRecommend())
                .isHot(input.getIsHot())
                .coverImageId(input.getCoverImageId())
                .build();
    }
} 