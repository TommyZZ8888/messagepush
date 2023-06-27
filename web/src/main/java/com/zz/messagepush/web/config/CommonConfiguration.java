package com.zz.messagepush.web.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.commons.compress.utils.Lists;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.List;

/**
 * @Description CommonConfiguration
 * @Author 张卫刚
 * @Date Created on 2023/6/27
 */

@Configuration
public class CommonConfiguration {

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter messageConverter = new FastJsonHttpMessageConverter();
        List<MediaType> mediaTypeList = Lists.newArrayList();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        messageConverter.setSupportedMediaTypes(mediaTypeList);
        return new HttpMessageConverters(messageConverter);
    }

}
