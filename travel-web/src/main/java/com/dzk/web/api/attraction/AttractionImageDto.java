package com.dzk.web.api.attraction;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data   
@Schema(description = "景点图片")
public class AttractionImageDto {

        
    @Schema(description = "景点图片id")
    private Long id;    

    
    @Schema(description = "景点id")
    private Long attractionId;


    @Schema(description = "文件uuid")
    private String CoverImageUuid;

    @Schema(description = "排序")
    private Integer sort;

    @Data
    @Schema(description = "景点图片输入数据传输对象")
    public static class Input {
        @Schema(description = "景点图片id")
        private Long imageId;

        @Schema(description = "文件id")
        private Long fileId;
        
        @Schema(description = "排序")
        private Integer sort;
    }

    
}