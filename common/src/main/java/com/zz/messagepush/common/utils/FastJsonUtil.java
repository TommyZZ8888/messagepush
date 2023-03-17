package com.zz.messagepush.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.zz.messagepush.common.anno.ConversionNumber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */
public class FastJsonUtil {

    public static <T> List<T> parseObject2List(String data, Class<T> clazz, String filed) {
        JSONObject jsonObject = JSON.parseObject(data);
        if (jsonObject == null) {
            return null;
        }
        JSONArray jsonArray = jsonObject.getJSONArray(filed);
        return parseArray(jsonArray, clazz);
    }

    public static <T> T parseObject(String data, Class<T> clazz, String filed) {
        JSONObject jsonObject = JSON.parseObject(data);
        if (jsonObject == null) {
            return null;
        }
        String str = jsonObject.getString(filed);
        return parseObject(str, clazz);
    }


    public static <T> T parseObject(String data, Class<T> clazz) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        if (jsonObject == null) {
            return null;
        }
        Field[] fields = clazz.getDeclaredFields();
        T result = jsonObject.toJavaObject(clazz);
        for (Field field : fields) {
            ConversionNumber conversionNumber = field.getAnnotation(ConversionNumber.class);
            JSONField jsonField = field.getAnnotation(JSONField.class);
            if (conversionNumber != null) {
                field.setAccessible(true);
                try {
                    double v;
                    if (jsonField != null) {
                        v = jsonObject.getDoubleValue(jsonField.name()) * conversionNumber.value();
                    } else {
                        v = jsonObject.getDoubleValue(field.getName()) * conversionNumber.value();
                    }
                    field.set(result, Double.valueOf(v).longValue());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }

    public static <T> List<T> parseArray(JSONArray jsonArray, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        jsonArray.forEach(item -> {
            T result = parseObject(JSON.toJSONString(item), clazz);
            list.add(result);
        });
        return list;
    }
}
