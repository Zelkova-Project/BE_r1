package com.zelkova.zelkova.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zelkova.zelkova.controller.formatter.LocalDateFormatter;

@Configuration
public class CustomServletConfig implements WebMvcConfigurer {
  
  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addFormatter(new LocalDateFormatter());
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")    // 모든 경로 체킹
            .allowedOrigins("**")  // 허락하는 origin
            .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS") // 허락하는 메소드
            .maxAge(30) // 30초간 preflight 허용(클라이언트와 stateless가 아닌 임시 접속 유지상태를 의미)
            .allowedHeaders("Authorization", "Cache-Control", "Content-Type"); // 허락된 Header 키값
  }
}
