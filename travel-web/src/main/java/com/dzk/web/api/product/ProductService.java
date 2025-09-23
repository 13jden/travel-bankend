package com.dzk.web.api.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzk.common.exception.BusinessException;
import com.dzk.web.file.FileEntity;
import com.dzk.web.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    @Autowired
    ProductMapper productMapper;
    
    @Autowired
    ProductImageMapper productImageMapper;
    
    @Autowired
    FileService fileService;

    /**
     * 搜索产品
     */
    public List<ProductDto> search(ProductCriteria criteria, Pageable pageable) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        
        if (criteria.getName() != null && !criteria.getName().isEmpty()) {
            queryWrapper.like("name", criteria.getName());
        }
        if (criteria.getNameEn() != null && !criteria.getNameEn().isEmpty()) {
            queryWrapper.like("name_en", criteria.getNameEn());
        }
        if (criteria.getCategoryId() != null) {
            queryWrapper.eq("category_id", criteria.getCategoryId());
        }
        if (criteria.getStatus() != null) {
            queryWrapper.eq("status", criteria.getStatus());
        }
        if (criteria.getIsRecommend() != null) {
            queryWrapper.eq("is_recommend", criteria.getIsRecommend());
        }
        if (criteria.getIsHot() != null) {
            queryWrapper.eq("is_hot", criteria.getIsHot());
        }
        queryWrapper.orderByDesc("create_time");
        
        Page<Product> page = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        Page<Product> productPage = this.page(page, queryWrapper);
        
        return ProductConverter.toDtoList(productPage.getRecords());
    }

    /**
     * 添加产品
     */
    @Transactional
    public ProductDto add(ProductDto.Input input) {
        try {
            // 1. 保存产品基本信息
            Product product = ProductConverter.toEntity(input);
            productMapper.insert(product);
            
            // 2. 处理产品图片
            if (input.getImages() != null && !input.getImages().isEmpty()) {
                saveProductImages(product.getId().toString(), input.getImages());
            }
            
            return getDetail(product.getId());
            
        } catch (Exception e) {
            throw new BusinessException("添加产品失败: " + e.getMessage());
        }
    }

    /**
     * 更新产品
     */
    @Transactional
    public ProductDto update(Long id, ProductDto.Input input) {
        try {
            Product existingProduct = productMapper.selectById(id);
            if (existingProduct == null) {
                throw new BusinessException("产品不存在！");
            }
            
            Product product = ProductConverter.toEntity(input);
            product.setId(id);
            productMapper.updateById(product);
            
            if (input.getImages() != null) {
                deleteProductImages(id.toString());
                if (!input.getImages().isEmpty()) {
                    saveProductImages(id.toString(), input.getImages());
                }
            }
            
            return getDetail(id);
            
        } catch (Exception e) {
            throw new BusinessException("更新产品失败: " + e.getMessage());
        }
    }

    /**
     * 获取产品详情
     */
    public ProductDto getDetail(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("产品不存在");
        }
        
        ProductDto dto = ProductConverter.toDto(product);
        
        List<ProductImageDto> images = getProductImages(id.toString());
        if (dto instanceof ProductDto.Detail) {
            ((ProductDto.Detail) dto).setImages(images);
        }
        
        return dto;
    }

    /**
     * 删除产品
     */
    @Transactional
    public ProductDto delete(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("产品不存在");
        }
        
        try {
            deleteProductImages(id.toString());
            productMapper.deleteById(id);
            
            return ProductConverter.toDto(product);
        } catch (Exception e) {
            throw new BusinessException("删除产品失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除产品
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        try {
            for (Long id : ids) {
                deleteProductImages(id.toString());
            }
            productMapper.deleteBatchIds(ids);
        } catch (Exception e) {
            throw new BusinessException("批量删除产品失败: " + e.getMessage());
        }
    }

    /**
     * 保存产品图片关联
     */
    private void saveProductImages(String productId, List<ProductImageDto.Input> imageInputs) {
        for (int i = 0; i < imageInputs.size(); i++) {
            ProductImageDto.Input imageInput = imageInputs.get(i);
            
            FileEntity fileEntity = fileService.getById(Long.valueOf(imageInput.getFileId()));
            if (fileEntity == null) {
                throw new BusinessException("文件不存在，文件ID: " + imageInput.getFileId());
            }
            
            ProductImage productImage = new ProductImage();
            productImage.setProductId(productId);
            productImage.setFileId(imageInput.getFileId());
            productImage.setSort(imageInput.getSort() != null ? imageInput.getSort() : i + 1);
            productImage.setIsDetail(imageInput.getIsDetail() != null ? imageInput.getIsDetail() : false);
            
            productImageMapper.insert(productImage);
        }
    }

    /**
     * 删除产品图片关联
     */
    private void deleteProductImages(String productId) {
        QueryWrapper<ProductImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        productImageMapper.delete(queryWrapper);
    }

    /**
     * 获取产品图片列表
     */
    private List<ProductImageDto> getProductImages(String productId) {
        QueryWrapper<ProductImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        queryWrapper.orderByAsc("sort");
        
        List<ProductImage> productImages = productImageMapper.selectList(queryWrapper);
        
        return productImages.stream().map(image -> {
            ProductImageDto dto = new ProductImageDto();
            dto.setId(image.getId());
            dto.setProductId(image.getProductId());
            dto.setFileId(image.getFileId());
            dto.setSort(image.getSort());
            dto.setIsDetail(image.getIsDetail());
            
            FileEntity fileEntity = fileService.getById(Long.valueOf(image.getFileId()));
            if (fileEntity != null) {
                dto.setFileUuid(fileEntity.getUuid());
            }
            
            return dto;
        }).collect(Collectors.toList());
    }
} 