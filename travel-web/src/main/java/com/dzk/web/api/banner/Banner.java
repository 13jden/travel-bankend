package com.dzk.web.api.banner;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "轮播图信息实体")
public class Banner {

  @Schema(description = "轮播图ID")
  @TableId(type = IdType.AUTO)
  private Long id;
  
  @Schema(description = "轮播图片")
  private Long imageId;
  
  @Schema(description = "轮播图类型")
  @TableField(value = "type")
  private String type;
  
  @Schema(description = "是否启用", example = "1")
  @TableField(value = "is_enable")
  private Boolean isEnable;
  
  @Schema(description = "轮播图文字描述")
  @TableField(value = "text")
  private String text;
  
  @Schema(description = "关联内容ID")
  @TableField(value = "content_id")
  private Long contentId;
  
  @Schema(description = "排序")
  @TableField(value = "sort")
  private Integer sort;

}
