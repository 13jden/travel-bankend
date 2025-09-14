package com.dzk.web.api.Agent.knowledge;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.dzk.common.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data   
public class Knowledge extends BaseEntity {
    @TableId(type = IdType.AUTO)
    @Schema(description = "知识ID")
    private String id;

    @TableField(value = "name")
    @Schema(description = "知识名称")
    private String name;

    @TableField(value = "description")
    @Schema(description = "知识描述")
    private String description;

    @TableField(value = "content")
    @Schema(description = "知识内容")
    private String content;

    @TableField(value = "type")
    @Schema(description = "知识类型")
    private String type;

    @TableField(value = "status")
    @Schema(description = "知识状态")
    private String status;

    @TableField(value = "error_message")
    @Schema(description = "错误信息")
    private String errorMessage;

}