package com.dzk.web.api.Agent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class ChatRequest {
    
//    @Schema(description = "儿童信息")
//    private ChildDto.Base childBaseInfo;

    @Schema(description = "用户问题")
    private String content;

    @Schema(description = "记忆内容")
    private List<Memory> memory;

    @Schema(description = "附件信息")
    private List<File> attachments;

    @Schema(description = "相关儿童id")
    private Long childId;

    @Data
    public static class Memory {
        @Schema(description = "角色")
        private String role;

        @Schema(description = "内容")
        private String content;
    }

}
