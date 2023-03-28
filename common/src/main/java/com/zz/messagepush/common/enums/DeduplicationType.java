package com.zz.messagepush.common.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/28
 */
public enum DeduplicationType {

    CONTENT(10, "N分钟相同内容去重"),
    FREQUENCY(20, "一天内N次相同渠道去重"),
    ;

    private Integer code;

    private String description;

    DeduplicationType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public static List<Integer> getDeduplicationList() {
        List<Integer> list = new ArrayList<>();
        for (DeduplicationType value : values()) {
            list.add(value.getCode());
        }
        return list;
    }


}
