package com.dzk.web.product;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.dzk.common.common.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;

@Service
public class Product extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private String id;

    @TableField(value = "name")
    @Schema(description = "文创产品名称")
    private String name;

    @TableField(value = "description")
    @Schema(description = "文创产品描述")
    private String description;

    @TableField(value = "price")
    @Schema(description = "文创产品价格")
    private String price;

    @TableField(value = "stock")
    @Schema(description = "文创产品库存")
    private String stock;

    @TableField(value = "category_id")
    @Schema(description = "文创产品分类id")
    private String categoryId;

    @TableField(value = "status")
    @Schema(description = "文创产品状态(是否上架)")
    private Boolean status;

    @TableField(value = "sales")
    @Schema(description = "文创产品销量")
    private String sales;

    @TableField(value = "is_recommend")
    @Schema(description = "是否推荐")
    private Boolean isRecommend;

    @TableField(value = "is_hot")
    @Schema(description = "是否热门")
    private Boolean isHot;

}