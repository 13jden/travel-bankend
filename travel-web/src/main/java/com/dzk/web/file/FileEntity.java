package com.dzk.web.file;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import com.dzk.common.common.BaseEntity;

@Data
@TableName("file")  // 添加这个注解指定表名
public class FileEntity extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "文件id")
    private Long id;
    
    @TableField(value = "uuid")
    @Schema(description = "文件uuid")
    private String uuid;
    
    @TableField(value = "path")
    @Schema(description = "文件路径")
    private String path;

    @TableField(value = "type")
    @Schema(description = "文件类型")
    private String type;

    @TableField(value = "extension")
    @Schema(description = "文件扩展名")
    private String extension;

    @TableField(value = "size")
    @Schema(description = "文件大小")
    private Long size;
}