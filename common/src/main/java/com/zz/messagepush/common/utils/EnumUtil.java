package com.zz.messagepush.common.utils;

import java.lang.reflect.Method;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */
public class EnumUtil {

    public static Object getCode(Class clazz, Object name) {
        if (name == null) {
            return null;
        }
        Object[] enumConstants = clazz.getEnumConstants();

        try {
            for (Object object : enumConstants) {
                Method getCode = clazz.getMethod("getCode");
                Method getName = clazz.getMethod("getDescription");

                if (getName.invoke(object).equals(name)) {
                    return getCode.invoke(object);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getValue(Class clazz, Object code) {
        if (code == null) {
            return null;
        }
        Object[] enumConstants = clazz.getEnumConstants();
        try {
            for (Object object : enumConstants) {
                Method getCode = clazz.getMethod("getCode");
                Method getDescription = clazz.getMethod("getDescription");
                if (getCode.invoke(object).equals(code)) {
                    return getDescription.invoke(object).toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static <E> E getEnumByCode(Class<E> clazz, Object code) {
        if (code == null) {
            return null;
        }
        E[] enumConstants = clazz.getEnumConstants();

        try {
            for (E e : enumConstants) {
                Method getCode = clazz.getMethod("getCode");
                if (getCode.invoke(e).equals(code)) {
                    return e;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
