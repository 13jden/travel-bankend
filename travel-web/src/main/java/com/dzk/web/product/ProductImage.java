package com.dzk.web.product;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.dzk.common.common.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;

@Service
public class ProductImage extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private String id;

    @TableField(value = "product_id")
    @Schema(description = "文创产品id") 
    private String productId;

    @TableField(value = "sort")
    @Schema(description = "文创产品图片排序")
    private Integer sort;

    @TableField(value = "is_detail")
    @Schema(description = "是否详情图")
    private Boolean isDetail;

    @TableField(value = "file_id")
    @Schema(description = "文件id")
    private String fileId;

    
}