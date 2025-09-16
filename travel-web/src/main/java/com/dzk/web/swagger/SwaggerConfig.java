package com.dzk.web.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Travel API Doc",
                description = "智慧旅游API文档",
                version = "0.1",
                contact = @Contact(name = "dzk", email = "ma-nbi@11.com")
        ),
        security = {@SecurityRequirement(name = "JWT")}
)
@Configuration
public class SwaggerConfig {

}
