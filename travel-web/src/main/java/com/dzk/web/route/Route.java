package com.dzk.web.route;

import java.util.List;

import com.dzk.common.common.BaseEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data           
public class Route extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "路线id")
    private Long id;

    @TableField(value = "title")
    @Schema(description = "路线标题")
    private String title;
    
    @TableField(value = "description")
    @Schema(description = "路线描述")
    private String description;

    @TableField(value = "suitable_age")
    @Schema(description = "适合人群")
    private String suitableAge;

    @TableField(value = "sort")
    @Schema(description = "路线排序")
    private Integer sort;

    @TableField(value = "description")
    @Schema(description = "路线描述")
    private String description;

    @TableField(value = "is_enable")
    @Schema(description = "是否启用")
    private Boolean isEnable;

    @TableField(value = "scenic_spots")
    @Schema(description = "景点(json数组)")
    private String scenicSpots;


    public List<String> getScenicSpotsList() {
        return JSON.parseArray(scenicSpots, String.class);
    }

    public void setScenicSpots(List<String> scenicSpotsList) {
        this.scenicSpots = JSON.toJSONString(scenicSpotsList);
    }

}