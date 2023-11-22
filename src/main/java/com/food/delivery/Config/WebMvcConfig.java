package com.food.delivery.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
  // static resrouces mapping
  @Override
  protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    log.info("static resources mapping start...");
    registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
    registry.addResourceHandler("/frontend/**").addResourceLocations("classpath:/frontend/");
  }
}
