package com.dzk.web.api.Agent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChatResponse {
    @Schema(description = "模型回复内容")
    private String reply;
}
