package com.dzk.web.attraction;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Attraction extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "景点id")
    private Long id;

    @TableField(value = "title")
    @Schema(description = "景点标题")
    private String title;
    
    @TableField(value = "description")
    @Schema(description = "景点描述")
    private String description;

    @TableField(value = "sort")
    @Schema(description = "景点排序")
    private Integer sort;

    @TableField(value = "cover_image_id")
    @Schema(description = "景点封面图片id")
    private Long coverImageId;
    
    @TableField(value = "is_enable")
    @Schema(description = "是否启用")
    private Boolean isEnable;

    @TableField(value = "description")
    @Schema(description = "景点描述")
    private String description;

    @TableField(value = "content")
    @Schema(description = "景点内容")
    private String content;
    
}