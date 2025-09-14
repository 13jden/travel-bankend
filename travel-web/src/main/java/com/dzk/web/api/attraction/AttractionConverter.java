package com.dzk.web.api.attraction;

import com.dzk.web.utils.SecurityUtil;
import com.dzk.web.api.user.User;
import org.springframework.stereotype.Component;

@Component
public class AttractionConverter {

    public AttractionDto toDto(Attraction attraction) {
        User.Language language = SecurityUtil.getCurrentUser().getLanguage();
        String title;
        String description;
        String content;
        if(language == User.Language.EN_US) {
            title = attraction.getTitleEn();
            description = attraction.getDescriptionEn();
            content = attraction.getContentEn();
        } else {
            title = attraction.getTitle();
            description = attraction.getDescription();
            content = attraction.getContent();
        }
        
        return AttractionDto.builder()
            .id(attraction.getId())
            .title(title)
            .description(description)
            .content(content)
            .coverImageId(attraction.getCoverImageId())
            .isEnable(attraction.getIsEnable())
            .build();
    }

    public Attraction toEntity(AttractionDto.Input attractionDto) {
        return Attraction.builder()
            .id(attractionDto.getId())
            .title(attractionDto.getTitle())
            .titleEn(attractionDto.getTitleEn())
            .description(attractionDto.getDescription())
            .descriptionEn(attractionDto.getDescriptionEn())
            .coverImageId(attractionDto.getCoverImageId())
            .isEnable(attractionDto.getIsEnable())
            .content(attractionDto.getContent())
            .build();
    }
}   