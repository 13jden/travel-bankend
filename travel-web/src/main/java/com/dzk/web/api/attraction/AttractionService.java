package com.dzk.web.api.attraction;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzk.common.exception.BusinessException;
import com.dzk.web.file.FileEntity;
import com.dzk.web.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttractionService extends ServiceImpl<AttractionMapper, Attraction> {

    @Autowired
    AttractionMapper attractionMapper;
    
    @Autowired
    AttractionImageMapper attractionImageMapper;
    
    @Autowired
    FileService fileService;

    @Value("${upload.commonPath}")
    private String filePath;

    /**
     * 搜索景点
     */
    public List<AttractionDto> search(AttractionCriteria criteria, Pageable pageable) {
        QueryWrapper<Attraction> queryWrapper = new QueryWrapper<>();
        
        if (criteria.getTitle() != null && !criteria.getTitle().isEmpty()) {
            queryWrapper.like("title", criteria.getTitle());
        }
        if (criteria.getTitleEn() != null && !criteria.getTitleEn().isEmpty()) {
            queryWrapper.like("title_en", criteria.getTitleEn());
        }
        if (criteria.getDescription() != null && !criteria.getDescription().isEmpty()) {
            queryWrapper.like("description", criteria.getDescription());
        }
        if (criteria.getIsEnable() != null) {
            queryWrapper.eq("is_enable", criteria.getIsEnable());
        }
        queryWrapper.orderByDesc("create_time");
        
        // 使用 Pageable 创建 MyBatis-Plus 的 Page 对象
        Page<Attraction> page = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        Page<Attraction> attractionPage = this.page(page, queryWrapper);
        
        return AttractionConverter.toDtoList(attractionPage.getRecords());
    }

    /**
     * 添加景点（包含图片管理）
     */
    @Transactional
    public AttractionDto add(AttractionDto.Input input) {
        try {
            // 1. 保存景点基本信息
            Attraction attraction = AttractionConverter.toEntity(input);
            attractionMapper.insert(attraction);
            
            // 2. 处理景点图片
            if (input.getImages() != null && !input.getImages().isEmpty()) {
                saveAttractionImages(attraction.getId(), input.getImages());
            }
            
            // 3. 返回完整的景点信息
            return getDetail(attraction.getId());
            
        } catch (Exception e) {
            throw new BusinessException("添加景点失败: " + e.getMessage());
        }
    }

    /**
     * 更新景点（包含图片管理）
     */
    @Transactional
    public AttractionDto update(Long id, AttractionDto.Input input) {
        try {
            // 1. 检查景点是否存在
            Attraction existingAttraction = attractionMapper.selectById(id);
            if (existingAttraction == null) {
                throw new BusinessException("景点不存在！");
            }
            
            // 2. 更新景点基本信息
            Attraction attraction = AttractionConverter.toEntity(input);
            attraction.setId(id);
            attractionMapper.updateById(attraction);
            
            // 3. 处理景点图片
            if (input.getImages() != null) {
                // 删除原有图片关联
                deleteAttractionImages(id);
                // 添加新图片关联
                if (!input.getImages().isEmpty()) {
                    saveAttractionImages(id, input.getImages());
                }
            }
            
            // 4. 返回完整的景点信息
            return getDetail(id);
            
        } catch (Exception e) {
            throw new BusinessException("更新景点失败: " + e.getMessage());
        }
    }

    /**
     * 获取景点详情（包含图片信息）
     */
    public AttractionDto getDetail(Long id) {
        Attraction attraction = attractionMapper.selectById(id);
        if (attraction == null) {
            throw new BusinessException("景点不存在");
        }
        
        AttractionDto dto = AttractionConverter.toDto(attraction);
        
        // 获取景点图片信息
        List<AttractionImageDto> images = getAttractionImages(id);
        if (dto instanceof AttractionDto.Detail) {
            ((AttractionDto.Detail) dto).setImages(images);
        }
        
        return dto;
    }

    /**
     * 删除景点（包含图片清理）
     */
    @Transactional
    public AttractionDto delete(Long id) {
        Attraction attraction = attractionMapper.selectById(id);
        if (attraction == null) {
            throw new BusinessException("景点不存在");
        }
        
        try {
            // 1. 删除景点图片关联
            deleteAttractionImages(id);
            
            // 2. 删除景点
            attractionMapper.deleteById(id);
            
            AttractionDto dto = AttractionConverter.toDto(attraction);
            return dto;
        } catch (Exception e) {
            throw new BusinessException("删除景点失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除景点
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        try {
            for (Long id : ids) {
                // 删除每个景点的图片关联
                deleteAttractionImages(id);
            }
            // 批量删除景点
            attractionMapper.deleteBatchIds(ids);
        } catch (Exception e) {
            throw new BusinessException("批量删除景点失败: " + e.getMessage());
        }
    }

    /**
     * 启用/禁用景点
     */
    public AttractionDto toggleEnable(Long id) {
        Attraction attraction = attractionMapper.selectById(id);
        if (attraction == null) {
            throw new BusinessException("景点不存在");
        }
        
        attraction.setIsEnable(!attraction.getIsEnable());
        attractionMapper.updateById(attraction);
        
        return getDetail(id);
    }

    /**
     * 保存景点图片关联
     */
    private void saveAttractionImages(Long attractionId, List<AttractionImageDto.Input> imageInputs) {
        for (int i = 0; i < imageInputs.size(); i++) {
            AttractionImageDto.Input imageInput = imageInputs.get(i);
            
            // 验证文件是否存在
            FileEntity fileEntity = fileService.getById(imageInput.getFileId());
            if (fileEntity == null) {
                throw new BusinessException("文件不存在，文件ID: " + imageInput.getFileId());
            }
            
            // 创建图片关联
            AttractionImage attractionImage = new AttractionImage();
            attractionImage.setAttractionId(attractionId);
            attractionImage.setFileId(imageInput.getFileId());
            attractionImage.setSort(imageInput.getSort() != null ? imageInput.getSort() : i + 1);
            
            attractionImageMapper.insert(attractionImage);
        }
    }

    /**
     * 删除景点图片关联
     */
    private void deleteAttractionImages(Long attractionId) {
        QueryWrapper<AttractionImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attraction_id", attractionId);
        attractionImageMapper.delete(queryWrapper);
    }

    /**
     * 获取景点图片列表
     */
    private List<AttractionImageDto> getAttractionImages(Long attractionId) {
        QueryWrapper<AttractionImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attraction_id", attractionId);
        queryWrapper.orderByAsc("sort");
        
        List<AttractionImage> attractionImages = attractionImageMapper.selectList(queryWrapper);
        
        return attractionImages.stream().map(image -> {
            AttractionImageDto dto = new AttractionImageDto();
            dto.setId(image.getId());
            dto.setAttractionId(image.getAttractionId());
            dto.setFileId(image.getFileId());
            dto.setSort(image.getSort());
            
            // 获取文件UUID
            FileEntity fileEntity = fileService.getById(image.getFileId());
            if (fileEntity != null) {
                dto.setFileUrl(filePath+fileEntity.getUuid());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 添加景点图片
     */
    @Transactional
    public AttractionImageDto addImage(Long attractionId, AttractionImageDto.Input input) {
        // 检查景点是否存在
        Attraction attraction = attractionMapper.selectById(attractionId);
        if (attraction == null) {
            throw new BusinessException("景点不存在");
        }
        
        // 验证文件是否存在
        FileEntity fileEntity = fileService.getById(input.getFileId());
        if (fileEntity == null) {
            throw new BusinessException("文件不存在");
        }
        
        // 创建图片关联
        AttractionImage attractionImage = new AttractionImage();
        attractionImage.setAttractionId(attractionId);
        attractionImage.setFileId(input.getFileId());
        attractionImage.setSort(input.getSort() != null ? input.getSort() : 999);
        
        attractionImageMapper.insert(attractionImage);
        
        // 返回DTO
        AttractionImageDto dto = new AttractionImageDto();
        dto.setId(attractionImage.getId());
        dto.setAttractionId(attractionImage.getAttractionId());
        dto.setFileUrl(filePath+fileEntity.getUuid());
        dto.setFileId(fileEntity.getId());
        dto.setSort(attractionImage.getSort());
        
        return dto;
    }

    /**
     * 删除景点图片
     */
    @Transactional
    public boolean deleteImage(Long imageId) {
        AttractionImage attractionImage = attractionImageMapper.selectById(imageId);
        if (attractionImage == null) {
            throw new BusinessException("图片不存在");
        }
        
        return attractionImageMapper.deleteById(imageId) > 0;
    }

    /**
     * 更新图片排序
     */
    @Transactional
    public boolean updateImageSort(Long imageId, Integer sort) {
        AttractionImage attractionImage = attractionImageMapper.selectById(imageId);
        if (attractionImage == null) {
            throw new BusinessException("图片不存在");
        }
        
        attractionImage.setSort(sort);
        return attractionImageMapper.updateById(attractionImage) > 0;
    }
}
