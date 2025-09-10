package com.dzk.web.file;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import com.dzk.common.common.BaseEntity;

@Data
public class File extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "文件id")
    private Long id;
    
    @TableField(value = "url")
    @Schema(description = "文件url")
    private String url;

    @TableField(value = "type")
    @Schema(description = "文件类型")
    private String type;

    @TableField(value = "size")
    @Schema(description = "文件大小")
    private Long size;

    @TableField(value = "name")
    @Schema(description = "文件名称")
    private String name;
}