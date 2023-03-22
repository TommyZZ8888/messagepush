package com.zz.messagepush.common.enums;

/**
 * @Description 发送的消息类型
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
public enum MessageType {

    NOTICE(10, "通知类信息","notice"),

    MARKETING(20, "营销类信息","marketing"),

    AUTH_CODE(30, "验证码信息","auth_code"),
    ;

    private Integer code;

    private String description;

    private String code_en;

    MessageType(Integer code, String description, String code_en) {
        this.code = code;
        this.description = description;
        this.code_en = code_en;
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

    public String getCode_en() {
        return code_en;
    }

    public void setCode_en(String code_en) {
        this.code_en = code_en;
    }
}
