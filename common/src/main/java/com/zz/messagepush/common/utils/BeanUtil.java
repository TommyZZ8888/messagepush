package com.zz.messagepush.common.utils;


import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;


import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;


public class BeanUtil {

    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

    public static <T> T copy(Object source, Class<T> target) {
        if (source == null || target == null) {
            return null;
        }
        try {
            T instance = target.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, K> List<K> copyList(List<T> source, Class<K> target) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        return source.stream().map(s -> copy(s, target)).collect(Collectors.toList());
    }
}
