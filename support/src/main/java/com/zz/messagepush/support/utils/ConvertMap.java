package com.zz.messagepush.support.utils;


import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Description for Amis  amis框架在表单回显的时候，不支持嵌套动态语法
 * 编写工具类将List/Object 铺平成 map
 * @Author 张卫刚
 * @Date Created on 2023/3/30
 */
public class ConvertMap {

    /**
     * 需要打散的字段（将json字符串打散为一个个字段返回）
     * （主要用于回显数据）
     */
    private static final List<String> FIELD_FLAT_NAME = List.of("msgContent");

    /**
     * 需要格式化jsonArray返回的字段
     */
    private static final List<String> PARSE_JSON_ARRAY = Arrays.asList("feedCards", "btns");

    /**
     * 钉钉工作消息实际的映射
     */
    private static final List<String> DING_DING_OA_FIELD = Arrays.asList("dingDingOaHead", "dingDingOaBody");

    /**
     * 钉钉oa字段名实际的映射
     */
    private static final Map<String, String> DING_DING_OA_NAME_MAPPING = new HashMap<>();

    static {
        DING_DING_OA_NAME_MAPPING.put("bgcolor", "dingDingOaHeadBgColor");
        DING_DING_OA_NAME_MAPPING.put("text", "dingDingOaHeadTitle");
        DING_DING_OA_NAME_MAPPING.put("title", "dingDingOaTitle");
        DING_DING_OA_NAME_MAPPING.put("image", "media_id");
        DING_DING_OA_NAME_MAPPING.put("author", "dingDingOaAuthor");
        DING_DING_OA_NAME_MAPPING.put("content", "dingDingOaContent");
    }


    public static <T> List<Map<String, Object>> flatFirst(List<T> param) throws IllegalAccessException {
        List<Map<String, Object>> result = new ArrayList<>();
        for (T t : param) {
            Map<String, Object> map = flatSingle(t);
            result.add(map);
        }
        return result;
    }


    /**
     * 主要兼容amis的回显
     *
     * @param obj
     * @param
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> flatSingle(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        Map<String, Object> map = new HashMap<>();
        for (Field field : fields) {
            if (FIELD_FLAT_NAME.contains(field.getName())) {
                String fieldValue = (String) ReflectUtil.getFieldValue(obj, field);
                JSONObject jsonObject = JSONObject.parseObject(fieldValue);
                for (String key : jsonObject.keySet()) {
                    /**
                     * 钉钉OA消息回显
                     */
                    if (DING_DING_OA_FIELD.contains(key)) {
                        JSONObject object = jsonObject.getJSONObject(key);
                        for (String objKey : object.keySet()) {
                            map.put(DING_DING_OA_NAME_MAPPING.get(objKey), object.getString(objKey));
                        }
                    } else if (PARSE_JSON_ARRAY.contains(key)) {
                        /**
                         * 部分字段直接传入数组，把数组直接返回
                         */
                        map.put(key, JSONArray.parseArray(jsonObject.getString(key)));
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
