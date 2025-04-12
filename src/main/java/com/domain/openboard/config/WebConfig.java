package com.domain.openboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// 모든 API 경로
                .allowedOrigins("http://localhost:3000") // 프론트 웹 도메인 주소
                .allowedMethods("GET", "POST", "PUT","PATCH", "DELETE") // HTTP 메서드
                .allowedHeaders("*") // 모든 헤더 적용
                .allowCredentials(true); // 쿠키 허용
    }
}
