package com.dzk.web.api.attraction;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzk.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttractionService extends ServiceImpl<AttractionMapper, Attraction> {

    @Autowired
    AttractionMapper attractionMapper;

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
     * 更新景点
     */
    public AttractionDto update(Long id, AttractionDto.Input input) {
        Attraction attraction = attractionMapper.selectById(id);
        if (attraction == null) {
            throw new BusinessException("景点不存在！");
        }
        
        Attraction newAttraction = AttractionConverter.toEntity(input);
        newAttraction.setId(id);
        attractionMapper.updateById(newAttraction);
        
        return AttractionConverter.toDto(newAttraction);
    }

    /**
     * 添加景点
     */
    @Transactional
    public AttractionDto add(AttractionDto.Input input) {
        Attraction attraction = AttractionConverter.toEntity(input);
        attractionMapper.insert(attraction);
        AttractionDto dto = AttractionConverter.toDto(attraction);
        return dto;
    }

    /**
     * 获取景点详情
     */
    public AttractionDto getDetail(Long id) {
        Attraction attraction = attractionMapper.selectById(id);
        if (attraction == null) {
            throw new BusinessException("景点不存在");
        }
        
        AttractionDto dto = AttractionConverter.toDto(attraction);
        return dto;
    }

    /**
     * 删除景点
     */
    public AttractionDto delete(Long id) {
        Attraction attraction = attractionMapper.selectById(id);
        if (attraction == null) {
            throw new  BusinessException("景点不存在");
        }
        
        try {
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
    public void deleteBatch(List<Long> ids) {
        try {
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
        
        AttractionDto dto = AttractionConverter.toDto(attraction);
        return dto;
    }
}
