package com.dzk.web.api.guide;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "攻略搜索条件")
public class GuideCriteria {

    @Schema(description = "攻略标题")
    private String title;

    @Schema(description = "攻略英文标题")
    private String titleEn;

    @Schema(description = "攻略类型")
    private String type;

    @Schema(description = "是否启用")
    private Boolean isEnable;

    @Schema(description = "是否推荐")
    private Boolean isRecommend;
}