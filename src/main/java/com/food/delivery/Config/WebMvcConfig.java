package com.food.delivery.Config;

import com.food.delivery.Helper.JacksonObjectMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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

  // extend the default MessageConverters in MVC
  @Override
  protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    // create new MessageConverter object
    MappingJackson2HttpMessageConverter messageConverter =
        new MappingJackson2HttpMessageConverter();

    // setup objectMapper
    messageConverter.setObjectMapper(new JacksonObjectMapper());

    // add the objectMapper to the conveters container in mvc framework
    // index 0 means prioritize this customized objectMapper
    converters.add(0, messageConverter);
  }
}
