package com.dzk.web.api.guide;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GuideConverter {

    /**
     * 实体转DTO
     */
    public static GuideDto toDto(Guide guide) {
        if (guide == null) {
            return null;
        }
        
        return GuideDto.builder()
                .id(guide.getId())
                .sort(guide.getSort())
                .coverImageId(guide.getCoverImageId())
                .title(guide.getTitle())
                .titleEn(guide.getTitleEn())
                .type(guide.getType())
                .isEnable(guide.getIsEnable())
                .isRecommend(guide.getIsRecommend())
                .clickCount(guide.getClickCount())
                .likeCount(guide.getLikeCount())
                .collectCount(guide.getCollectCount())
                .markdownUuid(guide.getMarkdownUuid())
                .build();
    }
    
    /**
     * 实体列表转DTO列表
     */
    public static List<GuideDto> toDtoList(List<Guide> guides) {
        if (guides == null) {
            return null;
        }
        
        return guides.stream()
                .map(GuideConverter::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 输入DTO转实体
     */
    public static Guide toEntity(GuideDto.Input input) {
        if (input == null) {
            return null;
        }
        
        return Guide.builder()
                .sort(input.getSort())
                .coverImageId(input.getCoverImageId())
                .title(input.getTitle())
                .titleEn(input.getTitleEn())
                .type(input.getType())
                .isEnable(input.getIsEnable())
                .isRecommend(input.getIsRecommend())
                .clickCount(0L)
                .likeCount(0L)
                .collectCount(0L)
                .build();
    }
} 