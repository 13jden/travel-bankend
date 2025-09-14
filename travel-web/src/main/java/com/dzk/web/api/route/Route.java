package com.dzk.web.api.route;

import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.dzk.common.common.BaseEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Route extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "路线id")
    private Long id;

    @TableField(value = "title")
    @Schema(description = "路线标题")
    private String title;

    @TableField(value = "title_en")
    @Schema(description = "路线英文标题")
    private String titleEn;

    @TableField(value = "description")
    @Schema(description = "路线描述")
    private String description;

    @TableField(value = "description_en")
    @Schema(description = "路线英文描述")
    private String descriptionEn;

    @TableField(value = "suitable_age")
    @Schema(description = "适合人群")
    private String suitableAge;

    @TableField(value = "suitable_age_en")
    @Schema(description = "适合人群英文")
    private String suitableAgeEn;

    @TableField(value = "sort")
    @Schema(description = "路线排序")
    private Integer sort;

    @TableField(value = "content")
    @Schema(description = "路线内容")
    private String content;

    @TableField(value = "content_en")
    @Schema(description = "路线英文内容")
    private String contentEn;

    @TableField(value = "is_enable")
    @Schema(description = "是否启用")
    private Boolean isEnable;

    @TableField(value = "scenic_ids")
    @Schema(description = "景点(json数组)")
    private String scenicIds;


    public static List<String> getIdsList(String scenicIds) {
        return JSON.parseArray(scenicIds, String.class);
    }

    public static String getScenicIds(List<String> scenicIdsList) {
        return  JSON.toJSONString(scenicIdsList);
    }

}