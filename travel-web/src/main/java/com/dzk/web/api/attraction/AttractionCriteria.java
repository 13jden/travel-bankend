package com.dzk.web.api.attraction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class AttractionCriteria {
    
    /**
     * 景点标题（模糊查询）
     */
    @Schema(description = "景点标题")
    private String title;
    
    /**
     * 景点英文标题（模糊查询）
     */
    @Schema(description = "景点英文标题")
    private String titleEn;
    
    /**
     * 景点描述（模糊查询）
     */
    @Schema(description = "景点描述")
    private String description;
    
    /**
     * 景点是否启用
     */
    @Schema(description = "景点是否启用")
    private Boolean isEnable;
    

}