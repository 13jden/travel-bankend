package com.dzk.web.api.route;

import com.dzk.web.api.user.User;
import com.dzk.web.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dzk.web.api.attraction.Attraction;
import com.dzk.web.api.attraction.AttractionMapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RouteConverter {

    @Autowired
    private AttractionMapper attractionMapper;

    public RouteDto toDto(Route route) {
        List<Attraction> scenicSpots = attractionMapper.selectList(new LambdaQueryWrapper<Attraction>().in(Attraction::getId, Route.getIdsList(route.getScenicIds())));
        List<Attraction.ScenicSpot> scenicSpotDtos = scenicSpots.stream().map(scenicSpot -> {
            Attraction.ScenicSpot scenicSpotDto = new Attraction.ScenicSpot();
            scenicSpotDto.setId(scenicSpot.getId());
            scenicSpotDto.setName(scenicSpot.getTitle());
            return scenicSpotDto;
        }).collect(Collectors.toList());
        User.Language language = SecurityUtil.getCurrentUser().getLanguage();
        String title;
        String description;
        String content;
        if(language == User.Language.EN_US) {
            title = route.getTitleEn();
            description = route.getDescriptionEn();
            content = route.getContentEn();
        } else {
            title = route.getTitle();
            description = route.getDescription();
            content = route.getContent();
        }
        return RouteDto.builder()
            .id(route.getId())
            .title(title)
            .description(description)
            .suitableAge(route.getSuitableAge())
            .content(content)
            .sort(route.getSort())
            .isEnable(route.getIsEnable())
            .scenicSpots(scenicSpotDtos)
            .build();
    }

    public Route toEntity(RouteDto.Input routeInput) {
        String scenicIds = Route.getScenicIds(routeInput.getScenicIds());
        return Route.builder()
            .id(routeInput.getId())
            .title(routeInput.getTitle())
            .titleEn(routeInput.getTitleEn())
            .description(routeInput.getDescription())
            .descriptionEn(routeInput.getDescriptionEn())
            .suitableAge(routeInput.getSuitableAge())
            .suitableAgeEn(routeInput.getSuitableAgeEn())
            .content(routeInput.getContent())
            .contentEn(routeInput.getContentEn())
            .sort(routeInput.getSort())
            .isEnable(routeInput.getIsEnable())
            .scenicIds(scenicIds)
            .build();
    }


}  