package com.dzk.admin.api.guide;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "导游信息实体")
public class Guide {

  @Schema(description = "导游ID")
  private long id;
  
  @Schema(description = "排序索引")
  private long index;
  
  @Schema(description = "导游图片")
  private String image;
  
  @Schema(description = "导游标题")
  private String title;
  
  @Schema(description = "导游类型")
  private String type;

}
