package com.dzk.admin.api.Agent;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgentManagerController {
    @GetMapping("/test")
    public String test() {
        return "docker配置测试🚀🚀🚀";
    }
}
