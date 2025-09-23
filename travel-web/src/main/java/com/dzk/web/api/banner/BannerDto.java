package com.dzk.web.api.banner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BannerDto {

    @Schema(description = "banner id")
    private Long id;

    @Schema(description = "banner 标题")
    private String title;

    @Schema(description = "banner 描述")
    private String description;

    @Schema(description = "图片文件id")
    private Long fileId;

    @Schema(description = "图片uuid")
    private Long uuid;

    @Schema(description = "banner 是否启用")
    private Boolean isEnable;

    @Schema(description = "banner 排序")
    private Integer sort;

}
