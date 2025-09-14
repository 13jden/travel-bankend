package com.dzk.web.api.guide;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "攻略数据传输对象")
public class GuideDto {

    @Schema(description = "攻略ID")
    private Long id;
    
    @Schema(description = "攻略排序")
    private Integer sort;
    
    @Schema(description = "封面图片ID")
    private Long coverImageId;
    
    @Schema(description = "封面图片URL")
    private String coverImageUrl;
    
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

    @Schema(description = "内容(markdown格式)")
    private String content;

    @Schema(description = "攻略英文内容")
    private String contentEn;

    @Schema(description = "点击量")
    private Long clickCount;  

    @Schema(description = "点赞量")
    private Long likeCount;

    @Schema(description = "收藏量")
    private Long collectCount;
    
    @Schema(description = "创建时间")
    private String createTime;
    
    @Schema(description = "更新时间")
    private String updateTime;

    @Data
    @Schema(description = "攻略输入对象")
    public class GuideInput {

        @Schema(description = "攻略排序")
        private Integer sort;
        
        @Schema(description = "封面图片ID")
        @NotNull(message = "封面图片ID不能为空")
        private Long coverImageId;
        
        @Schema(description = "攻略标题")
        @NotBlank(message = "攻略标题不能为空")
        private String title;

        @Schema(description = "攻略英文标题")
        private String titleEn;
        
        @Schema(description = "攻略类型")
        @NotBlank(message = "攻略类型不能为空")
        private String type;

        @Schema(description = "是否启用")
        private Boolean isEnable = true;

        @Schema(description = "是否推荐")
        private Boolean isRecommend = false;

        @Schema(description = "内容(markdown格式)")
        @NotBlank(message = "攻略内容不能为空")
        private String content;

        @Schema(description = "攻略英文内容")
        private String contentEn;
    }
}   