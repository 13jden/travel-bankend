package com.dzk.web.api.guide;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dzk.common.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("guide")
public class Guide extends BaseEntity {

    @Schema(description = "攻略ID")
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @Schema(description = "攻略排序")
    @TableField(value = "sort")
    private Integer sort;
    
    @Schema(description = "封面图片id (file表id)")
    @TableField(value = "cover_image_id")
    private Long coverImageId;
    
    @Schema(description = "攻略标题")
    @TableField(value = "title")
    private String title;

    @Schema(description = "攻略英文标题")
    @TableField(value = "title_en")
    private String titleEn;
    
    @Schema(description = "攻略类型")
    @TableField(value = "type")
    private String type;

    @Schema(description = "是否启用")
    @TableField(value = "is_enable")
    private Boolean isEnable;

    @Schema(description = "是否推荐")
    @TableField(value = "is_recommend")
    private Boolean isRecommend;

    @Schema(description = "点击量")
    @TableField(value = "click_count")
    private Long clickCount;  

    @Schema(description = "点赞量")
    @TableField(value = "like_count")
    private Long likeCount;

    @Schema(description = "收藏量")
    @TableField(value = "collect_count")
    private Long collectCount;

    @Schema(description = "中文markdown文件UUID")
    @TableField(value = "markdown_uuid")
    private String markdownUuid;

    @Schema(description = "英文markdown文件UUID")
    @TableField(value = "markdown_uuid_en")
    private String markdownUuidEn;
}
