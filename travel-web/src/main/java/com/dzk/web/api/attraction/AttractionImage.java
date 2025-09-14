package com.dzk.web.api.attraction;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import com.dzk.common.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AttractionImage extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "景点图片id")
    private Long id;    

    @TableField(value = "attraction_id")
    @Schema(description = "景点id")
    private Long attractionId;

    @TableField(value = "file_id")
    @Schema(description = "文件id")
    private Long fileId;

    @TableField(value = "sort")
    @Schema(description = "排序")
    private Integer sort;

    
}
