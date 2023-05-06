package com.zz.messagepush.support.utils;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description for Amis  amis框架在表单回显的时候，不支持嵌套动态语法
 * 编写工具类将List/Object 铺平成 map
 * @Author 张卫刚
 * @Date Created on 2023/3/30
 */
public class ConvertMap {


    public static <T> List<Map<String, Object>> flatFirst(List<T> param, List<String> fieldName) throws IllegalAccessException {
        List<Map<String, Object>> result = new ArrayList<>();
        for (T t : param) {
            Map<String, Object> map = flatSingle(t, fieldName);
            result.add(map);
        }
        return result;
    }


    /**
     * 主要兼容amis的回显
     * @param obj
     * @param fieldName
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> flatSingle(Object obj, List<String> fieldName) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            //msgContent需要打散
            if (fieldName.contains(field.getName())) {
                JSONObject jsonObject;
                Object value = field.get(obj);
                if (value instanceof String) {
                    jsonObject = JSONObject.parseObject((String) value);
                } else {
                    jsonObject = JSONObject.parseObject(JSON.toJSONString(value));
                }
                for (String key : jsonObject.keySet()) {
                    if ("btns".equals(key) || "feedCards".equals(key)) {
                        map.put(key, JSON.parseArray(jsonObject.getString(key)));
                    } else {
                        map.put(key, jsonObject.getString(key));
                    }
                }
            }
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }


}
