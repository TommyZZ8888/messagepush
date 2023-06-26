package com.zz.messagepush.common.advice;

import com.zz.messagepush.common.anno.AustinResult;
import com.zz.messagepush.common.domain.ResponseResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @Description AustinResponseBodyAdvice
 * @Author 张卫刚
 * @Date Created on 2023/6/26
 */
@ControllerAdvice(basePackages = "com.zz.austin.web.controller")
public class AustinResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private static final String RETURN_CLASS = "ResponseResult";

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.getContainingClass().isAnnotationPresent(AustinResult.class) || methodParameter.hasMethodAnnotation(AustinResult.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
       if (Objects.nonNull(body) && Objects.nonNull(body.getClass())){
           String simpleName = body.getClass().getSimpleName();
           if (RETURN_CLASS.equals(simpleName)){
               return body;
           }
       }
        return ResponseResult.success();
    }
}
