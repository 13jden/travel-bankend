package com.dzk.web.file;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDto {

    @TableId(type = IdType.AUTO)
    @Schema(description = "文件id")
    private Long id;

    @TableField(value = "uuid")
    @Schema(description = "文件uuid")
    private String uuid;

    @TableField(value = "url")
    @Schema(description = "文件获取url")
    private String url;

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
