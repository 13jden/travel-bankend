package com.dzk.web.api.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "产品搜索条件")
public class ProductCriteria {

    @Schema(description = "产品名称")
    private String name;

    @Schema(description = "产品英文名称")
    private String nameEn;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "产品状态")
    private Boolean status;

    @Schema(description = "是否推荐")
    private Boolean isRecommend;

    @Schema(description = "是否热门")
    private Boolean isHot;
} 