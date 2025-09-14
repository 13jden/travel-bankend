package com.dzk.web.api.route;

import com.dzk.web.api.attraction.Attraction;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteDto {

    @Schema(description = "路线id")
    private Long id;

    @Schema(description = "路线标题")
    private String title;

    @Schema(description = "路线描述")
    private String description;

    @Schema(description = "路线英文标题")
    private String titleEn;

    @Schema(description = "路线英文描述")
    private String descriptionEn;

    @Schema(description = "路线适合人群")
    private String suitableAge;

    @Schema(description = "路线英文适合人群")
    private String suitableAgeEn;

    @Schema(description = "路线内容")
    private String content;

    @Schema(description = "路线英文内容")
    private String contentEn;

    @Schema(description = "路线排序")
    private Integer sort;

    @Schema(description = "路线是否启用")
    private Boolean isEnable;

    @Schema(description = "路线景点")
    private List<Attraction.ScenicSpot> scenicSpots;


    @Data
    @Schema(description = "路线输入数据传输对象")
    public static class Input {

        @Schema(description = "路线id")
        private Long id;

        @Schema(description = "路线标题")
        private String title;

        @Schema(description = "路线标题英文")
        private String titleEn;

        @Schema(description = "路线描述")
        private String description;

        @Schema(description = "路线描述英文")
        private String descriptionEn;

        @Schema(description = "路线适合人群")
        private String suitableAge;

        @Schema(description = "路线适合人群英文")
        private String suitableAgeEn;

        @Schema(description = "路线内容")
        private String content;

        @Schema(description = "路线英文内容")
        private String contentEn;

        @Schema(description = "路线排序")
        private Integer sort;

        @Schema(description = "路线是否启用")
        private Boolean isEnable;

        @Schema(description = "路线景点")
        private List<String> scenicIds;
    }   
    
}  