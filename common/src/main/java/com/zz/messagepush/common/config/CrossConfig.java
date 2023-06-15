package com.zz.messagepush.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description 跨域配置
 * @Author 张卫刚
 * @Date Created on 2023/6/15
 */
@Configuration
public class CrossConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/**")
              .allowCredentials(true)
              .allowedHeaders("*")
              .allowedOriginPatterns("*")
              .allowedMethods("GET","POST","HEAD","PUT","DELETE","OPTIONS")
              .maxAge(3600);
    }
}
