package com.dzk.web.api.attraction;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import com.dzk.common.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Attraction extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "景点id")
    private Long id;

    @TableField(value = "title")
    @Schema(description = "景点标题")
    private String title;

    @TableField(value = "title_en")
    @Schema(description = "景点英文标题")
    private String titleEn;
    
    @TableField(value = "description")
    @Schema(description = "景点描述")
    private String description;

    @TableField(value = "description_en")
    @Schema(description = "景点英文描述")
    private String descriptionEn;

    @TableField(value = "sort")
    @Schema(description = "景点排序")
    private Integer sort;

    @TableField(value = "cover_image_id")
    @Schema(description = "景点封面图片id")
    private Long coverImageId;
    
    @TableField(value = "is_enable")
    @Schema(description = "是否启用")
    private Boolean isEnable;

    @TableField(value = "content")
    @Schema(description = "景点内容")
    private String content;

    @TableField(value = "content_en")
    @Schema(description = "景点英文内容")
    private String contentEn;

    @TableField(value = "longitude")
    @Schema(description = "经度")
    private Double longitude;
    
    @TableField(value = "latitude")
    @Schema(description = "纬度")
    private Double latitude;

    @Data
    @Schema(description = "路线景点")
    public static class ScenicSpot {
        @Schema(description = "景点id")
        private Long id;
        @Schema(description = "景点名称")
        private String name;
    }
    
}