package com.dzk.web.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/temp/**")
                .addResourceLocations("file:D:/IdousoFile/temp/");

        registry.addResourceHandler("/images/banner/**")
                .addResourceLocations("file:D:/IdousoFile/banner/");

        registry.addResourceHandler("/images/avatar/**")
                .addResourceLocations("file:D:/IdousoFile/avatar/");

        registry.addResourceHandler("/video/**")
                .addResourceLocations("file:D:/IdousoFile/manuscript/video/");
        registry.addResourceHandler("/images/manuscript/**")
                .addResourceLocations("file:D:/IdousoFile/manuscript/images/");
        registry.addResourceHandler("/images/comment/**")
                .addResourceLocations("file:D:/IdousoFile/comment/");
        registry.addResourceHandler("/files/message/**")
                .addResourceLocations("file:D:/IdousoFile/message/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081", "http://localhost:8082","http://localhost:5173","http://localhost:5174")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

} 