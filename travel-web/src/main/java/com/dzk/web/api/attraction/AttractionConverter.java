package com.dzk.web.api.attraction;

import com.dzk.web.utils.SecurityUtil;
import com.dzk.web.api.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AttractionConverter {

    /**
     * 实体转DTO
     */
    public static AttractionDto toDto(Attraction attraction) {
        if (attraction == null) {
            return null;
        }
        
        return AttractionDto.builder()
                .id(attraction.getId())
                .title(attraction.getTitle())
                .titleEn(attraction.getTitleEn())
                .description(attraction.getDescription())
                .descriptionEn(attraction.getDescriptionEn())
                .sort(attraction.getSort())
                .coverImageId(attraction.getCoverImageId())
                .isEnable(attraction.getIsEnable())
                .content(attraction.getContent())
                .contentEn(attraction.getContentEn())
                .build();
    }
    
    /**
     * 实体列表转DTO列表
     */
    public static List<AttractionDto> toDtoList(List<Attraction> attractions) {
        if (attractions == null) {
            return null;
        }
        
        return attractions.stream()
                .map(AttractionConverter::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 输入DTO转实体
     */
    public static Attraction toEntity(AttractionDto.Input input) {
        if (input == null) {
            return null;
        }
        
        return Attraction.builder()
                .title(input.getTitle())
                .titleEn(input.getTitleEn())
                .description(input.getDescription())
                .descriptionEn(input.getDescriptionEn())
                .content(input.getContent())
                .contentEn(input.getContentEn())
                .coverImageId(input.getCoverImageId())
                .isEnable(input.getIsEnable())
                .build();
    }
}   