package com.zz.messagepush.common.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zz.messagepush.common.anno.ConversionNumber;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */

@ControllerAdvice
@Slf4j
@Order(2)
public class RequestAdvice implements RequestBodyAdvice {


    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return checkSupport(methodParameter, targetType);
    }

    public boolean checkSupport(MethodParameter methodParameter, Type targetType) {
        if (List.class.isAssignableFrom(methodParameter.getParameterType())) {
            Class<?> clz = (Class<?>) ((ParameterizedType) targetType).getActualTypeArguments()[0];
            return Arrays.stream(clz.getDeclaredFields()).anyMatch(this::checkSupport);
        }
        return Arrays.stream(methodParameter.getParameterType().getDeclaredFields()).anyMatch(this::checkSupport);
    }


    public boolean checkSupport(Field field) {
        if (field.getAnnotation(ConversionNumber.class) != null) {
            return true;
        }
        if (List.class.isAssignableFrom(field.getType())) {
            Type type = field.getGenericType();
            if (type instanceof ParameterizedType) {
                Class<?> clz = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
                return checkListClassReflect(clz.getDeclaredFields());
            }
        }
        Field[] fields = field.getClass().getDeclaredFields();
        return fields.length > 0 && Arrays.stream(fields).anyMatch(item -> item.getAnnotation(ConversionNumber.class) != null);
    }


    private boolean checkListClassReflect(Field[] fields) {
        return Arrays.stream(fields).anyMatch(this::checkSupport);
    }


    /**
     * 嵌套处理
     *
     * @param fields
     * @param jsonObject
     */
    public void conversionNumber(Field[] fields, JSONObject jsonObject) {
        for (Field field : fields) {
            ConversionNumber conversionNumber = field.getAnnotation(ConversionNumber.class);
            if (List.class.isAssignableFrom(field.getType())) {
                Type genericType = field.getGenericType();
                Class<?> clazz = (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
                Field[] fieldList = clazz.getDeclaredFields();
                JSONArray jsonArray = jsonObject.getJSONArray(field.getName());
                if (jsonArray != null && jsonArray.size() > 0 && !checkObjectIsSysType(clazz)) {
                    for (Object o : jsonArray) {
                        conversionNumber(fieldList, (JSONObject) o);
                    }
                }
            } else {
                Field[] declaredFields = field.getClass().getDeclaredFields();
                if (declaredFields.length > 0 && !checkObjectIsSysType(field.getType())) {
                    conversionNumber(declaredFields, jsonObject);
                }
            }
            if (conversionNumber != null) {
                field.setAccessible(true);
                String value = jsonObject.getString(field.getName());
                double parseDouble = Double.parseDouble(("".equals(value) || value == null) ? "0" : value);
                jsonObject.put(field.getName(), parseDouble * conversionNumber.value());
            }
        }
    }


    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        String bodyString = null;
        String result;
        try {
            InputStream stream = inputMessage.getBody();
            bodyString = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!StringUtils.isBlank(bodyString) && this.checkSupport(parameter, targetType)) {
            if (List.class.isAssignableFrom(parameter.getParameterType())) {
                Class<?> clazz = (Class<?>) ((ParameterizedType) targetType).getActualTypeArguments()[0];
                //获取list元素的字段
                Field[] declaredFields = clazz.getDeclaredFields();
                JSONArray jsonArray = JSONObject.parseArray(bodyString);
                List<?> list = new ArrayList<>();
                Class<?> className = null;
                try {
                    className = Class.forName(clazz.getTypeName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                for (Object o : jsonArray.toArray()) {
                    conversionNumber(declaredFields, (JSONObject) o);
                    list.add(((JSONObject) o).toJavaObject((Type) className));
                }
                result = JSONObject.toJSONString(list);
            } else {
                Field[] fields = ((Class<?>) targetType).getDeclaredFields();
                JSONObject jsonObject = JSON.parseObject(bodyString);
                conversionNumber(fields, jsonObject);
                result = jsonObject.toJSONString();
            }
        } else {
            result = bodyString;
        }
        InputStream arrayInputStream = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() {
                return arrayInputStream;
            }

            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }
        };
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return null;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return null;
    }


    /**
     * 检查object是否为java基本数据类型/包装类/java.util.date/java.sql.date
     *
     * @param object
     * @return
     */
    public static boolean checkObjectIsSysType(Object object) {
        String objType = object.toString();
        if ("byte".equals(objType) || "short".equals(objType) || "int".equals(objType) || "long".equals(objType) || "double".equals(objType) || "boolean".equals(objType) || "char".equals(objType) || "float".equals(objType)) {
            return true;
        } else if ("class java.util.Date".equals(objType) || "class java.lang.Byte".equals(objType) || "class java.lang.Short".equals(objType) || "class java.lang.Integer".equals(objType) || "class java.lang.Long".equals(objType) || "class java.lang.Double".equals(objType) || "class java.lang.Float".equals(objType) || "class java.lang.Boolean".equals(objType) || "class java.lang.String".equals(objType)) {
            return true;
        } else {
            return false;
        }
    }
}
