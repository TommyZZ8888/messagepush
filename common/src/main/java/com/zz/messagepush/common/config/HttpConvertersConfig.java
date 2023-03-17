package com.zz.messagepush.common.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zz.messagepush.common.anno.ConversionNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */

@Configuration
@Slf4j
public class HttpConvertersConfig {


    @Value("${spring.date.format}")
    private String dateFormat;

    /**
     * 启用fastjson的转换
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.DisableCircularReferenceDetect
        );
        fastJsonConfig.setDateFormat(dateFormat);
        fastJsonConfig.setSerializeFilters((ValueFilter) this::ConversionNumberFilter);
        fastJsonConverter.setFastJsonConfig(fastJsonConfig);
        fastJsonConverter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON));
        return new HttpMessageConverters(fastJsonConverter);
    }


    /**
     * 接口返回的数据对象中有ConversionNumber注解的，换算后返回
     *
     * @param object
     * @param name
     * @param value
     * @return
     */
    private Object ConversionNumberFilter(Object object, String name, Object value) {
        try {
            Field declaredField = object.getClass().getDeclaredField(name);
            ConversionNumber annotation = declaredField.getAnnotation(ConversionNumber.class);
            if (annotation != null) {
                if (value == null) {
                    return null;
                }
                double tempValue = Double.parseDouble(value.toString());
                return tempValue / annotation.value();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return value;
    }
}
