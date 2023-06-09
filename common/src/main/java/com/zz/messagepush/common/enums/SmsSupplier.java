package com.zz.messagepush.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Description SmsSupplier
 * @Author 张卫刚
 * @Date Created on 2023/6/9
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SmsSupplier {

    TENCENT(10, "腾讯渠道商"),
    YUN_PIAN(20, "云片渠道商"),
    ;

    private Integer code;
    private String description;


    /**
     * 根据状态获取描述信息
     * @param code
     * @return
     */
    public static String getDescriptionByStatus(Integer code) {
        for (SmsSupplier value : values()) {
            if (code.equals(value.getCode())) {
                return value.getDescription();
            }
        }
        return "";
    }
}
