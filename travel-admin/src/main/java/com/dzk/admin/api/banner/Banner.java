package com.dzk.admin.api.banner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "轮播图信息实体")
public class Banner {

  @Schema(description = "轮播图ID")
  private Integer id;
  
  @Schema(description = "轮播图片")
  private String image;
  
  @Schema(description = "轮播图类型")
  private Integer type;
  
  @Schema(description = "是否激活", example = "1")
  private Integer isActive;
  
  @Schema(description = "轮播图文字描述")
  private String text;
  
  @Schema(description = "关联内容ID")
  private Integer contentId;
  
  @Schema(description = "排序权重")
  private Integer sort;

}
