package com.dzk.web.api.product.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    @Schema(description = "分类id")
    private Long id;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "分类英文名称")
    private String nameEn;

    @Schema(description = "分类排序")
    private Integer sort;

    @Schema(description = "分类是否启用")
    private Boolean isEnable;

    @Data
    @Schema(description = "分类输入数据传输对象")
    public static class Input {

        @Schema(description = "分类名称")
        private String name;

        @Schema(description = "分类英文名称")
        private String nameEn;

        @Schema(description = "分类排序")
        private Integer sort;

        @Schema(description = "分类是否启用")
        private Boolean isEnable;
    }
}   