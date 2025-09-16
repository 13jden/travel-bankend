package com.dzk.admin.api.Agent;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "AgentManagerController", description = "AI Agent管理")
public class AgentManagerController {
    @GetMapping("/test")
    public String test() {
        return "docker配置测试🚀🚀🚀";
    }
}
