package com.dzk.web.api.Agent;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("chat")
public class Chat {

    @TableId(type = IdType.AUTO)
    private String id;

    @TableField(value = "content")
    private String content;

    @TableField(value = "create_time")
    private String createTime;

    @TableField(value = "update_time")
    private String updateTime;

    @TableField(value = "status")
    private String status;

    @Schema(description = "用户ID")
    @TableField(value = "user_id")
    private Long userId;

}   