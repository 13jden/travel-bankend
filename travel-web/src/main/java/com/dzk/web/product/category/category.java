package com.dzk.web.product.category;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import com.dzk.common.common.BaseEntity;

@Data
public class Category extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "分类id")
    private Long id;

    @TableField(value = "name")
    @Schema(description = "分类名称")
    private String name;

    @TableField(value = "sort")
    @Schema(description = "排序")
    private Integer sort;

    @TableField(value = "is_enable")
    @Schema(description = "状态(是否启用)")
    private Boolean isEnable;
    
}