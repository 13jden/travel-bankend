package com.dzk.web.api.product;    

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    @Schema(description = "文创产品id")
    private Long id;

    @Schema(description = "文创产品名称")
    private String name;

    @Schema(description = "文创产品英文名称")
    private String nameEn;

    @Schema(description = "文创产品描述")
    private String description;

    @Schema(description = "文创产品英文描述")
    private String descriptionEn;

    @Schema(description = "文创产品价格")
    private BigDecimal price;

    @Schema(description = "文创产品库存")
    private Integer stock;

    @Schema(description = "文创产品分类id")
    private Long categoryId;

    @Schema(description = "文创产品状态(是否上架)")
    private Boolean status;

    @Schema(description = "文创产品销量")
    private Integer sales;

    @Schema(description = "是否推荐")
    private Boolean isRecommend;

    @Schema(description = "是否热门")
    private Boolean isHot;

    @Schema(description = "封面图片id")
    private Long coverImageId;

    @Data
    @Schema(description = "产品详情")
    public static class Detail extends ProductDto {
        @Schema(description = "产品图片列表")
        private List<ProductImageDto> images;
    }

    @Data
    @Schema(description = "产品输入数据传输对象")
    public static class Input {
        @Schema(description = "文创产品名称")
        private String name;

        @Schema(description = "文创产品英文名称")
        private String nameEn;

        @Schema(description = "文创产品描述")
        private String description;

        @Schema(description = "文创产品英文描述")
        private String descriptionEn;

        @Schema(description = "文创产品价格")
        private BigDecimal price;

        @Schema(description = "文创产品库存")
        private Integer stock;

        @Schema(description = "文创产品分类id")
        private Long categoryId;

        @Schema(description = "文创产品状态(是否上架)")
        private Boolean status;

        @Schema(description = "是否推荐")
        private Boolean isRecommend;

        @Schema(description = "是否热门")
        private Boolean isHot;

        @Schema(description = "封面图片id")
        private Long coverImageId;

        @Schema(description = "产品图片列表")
        private List<ProductImageDto.Input> images;
    }
}   