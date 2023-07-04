package com.zz.messagepush.common.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description EnumUtils
 * @Author 张卫刚
 * @Date Created on 2023/7/4
 */
public class EnumUtils {
    private EnumUtils() {
    }

    public static <T extends PowerfulEnum> String getDescriptionByCode(Integer code, Class<T> clz) {
        return Arrays.stream(clz.getEnumConstants())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst().map(PowerfulEnum::getDescription).orElse("");
    }

    public static <T extends PowerfulEnum> T getEnumByCode(Integer code, Class<T> clz) {
        return Arrays.stream(clz.getEnumConstants())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst().orElse(null);
    }


    public static <T extends PowerfulEnum> Integer getCodeByDescription(String description, Class<T> clz) {
        return Arrays.stream(clz.getEnumConstants())
                .filter(e -> Objects.equals(e.getDescription(), description))
                .findFirst().map(PowerfulEnum::getCode).orElse(null);
    }

    public static <T extends PowerfulEnum> List<Integer> getCodeList(Class<T> clz) {
        return Arrays.stream(clz.getEnumConstants())
                .map(PowerfulEnum::getCode).collect(Collectors.toList());
    }


}
