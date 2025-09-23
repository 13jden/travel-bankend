package com.dzk.web.api.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.dzk.common.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "文创产品id")
    private Long id;

    @TableField(value = "name")
    @Schema(description = "文创产品名称")
    private String name;

    @TableField(value = "name_en")
    @Schema(description = "文创产品英文名称")
    private String nameEn;

    @TableField(value = "description")
    @Schema(description = "文创产品描述")
    private String description;

    @TableField(value = "description_en")
    @Schema(description = "文创产品英文描述")
    private String descriptionEn;

    @TableField(value = "price")
    @Schema(description = "文创产品价格")
    private BigDecimal price;

    @TableField(value = "stock")
    @Schema(description = "文创产品库存")
    private Integer stock;

    @TableField(value = "category_id")
    @Schema(description = "文创产品分类id")
    private Long categoryId;

    @TableField(value = "status")
    @Schema(description = "文创产品状态(是否上架)")
    private Boolean status;

    @TableField(value = "sales")
    @Schema(description = "文创产品销量")
    private Integer sales;

    @TableField(value = "is_recommend")
    @Schema(description = "是否推荐")
    private Boolean isRecommend;

    @TableField(value = "is_hot")
    @Schema(description = "是否热门")
    private Boolean isHot;

    @TableField(value = "cover_image_id")
    @Schema(description = "封面图片id")
    private Long coverImageId;
}