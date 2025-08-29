package com.dzk.web.Agent;

import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DashScopeConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    @Value("${spring.ai.dashscope.agent.app-id}")
    private String appId;

    @Bean
    public DashScopeAgentApi dashScopeAgentApi() {
        // 直接使用源码中的构造函数
        return new DashScopeAgentApi(apiKey);
    }
}