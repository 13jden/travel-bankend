package com.dzk.web.api.attraction;

import java.util.List;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttractionDto {

    @Schema(description = "景点id")
    private Long id;

    @Schema(description = "景点标题")
    private String title;

    @Schema(description = "景点英文标题")
    private String titleEn;

    @Schema(description = "景点描述")
    private String description;

    @Schema(description = "景点英文描述")
    private String descriptionEn;

    @Schema(description = "景点排序")
    private Integer sort;

    @Schema(description = "景点封面图片id")
    private String coverImageUuid;

    @Schema(description = "景点是否启用")
    private Boolean isEnable;

    @Schema(description = "景点内容")
    private String content;

    @Schema(description = "景点英文内容")
    private String contentEn;

    @Data
    @Schema(description = "景点详情")
    public static class Detail extends AttractionDto {
       
       @Schema(description = "景点图片列表")
       private List<AttractionImageDto> images;
    }

    @Data
    @Schema(description = "景点输入数据传输对象")
    public static class Input {

        @Schema(description = "景点标题")
        private String title;

        @Schema(description = "景点英文标题")
        private String titleEn;

        @Schema(description = "景点描述")
        private String description;

        @Schema(description = "景点英文描述")
        private String descriptionEn;

        @Schema(description = "景点内容")
        private String content;

        @Schema(description = "景点英文内容")
        private String contentEn;

        @Schema(description = "景点排序")
        private Integer sort;

        @Schema(description = "景点封面图片id")
        private Long coverImageId;

        @Schema(description = "景点是否启用")
        private Boolean isEnable;

        @Schema(description = "景点图片列表")
        private List<AttractionImageDto.Input> images;
    }
}