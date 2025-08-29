package com.dzk.common.common;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;

@Data
@Schema(description = "基础实体类")
public class BaseEntity {
    
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private Date updateTime;

}