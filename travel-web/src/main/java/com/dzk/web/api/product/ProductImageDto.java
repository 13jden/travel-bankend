package com.dzk.web.api.product;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data   
@Schema(description = "产品图片")
public class ProductImageDto {

    @Schema(description = "产品图片id")
    private String id;    

    @Schema(description = "产品id")
    private String productId;

    @Schema(description = "文件id")
    private String fileId;

    @Schema(description = "文件uuid")
    private String fileUuid;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "是否详情图")
    private Boolean isDetail;

    @Data
    @Schema(description = "产品图片输入数据传输对象")
    public static class Input {
        @Schema(description = "文件id")
        private String fileId;
        
        @Schema(description = "排序")
        private Integer sort;
        
        @Schema(description = "是否详情图")
        private Boolean isDetail;
    }
} 