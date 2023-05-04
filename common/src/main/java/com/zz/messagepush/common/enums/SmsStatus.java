package com.zz.messagepush.common.enums;

/**
 * @Description 消息状态
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
public enum SmsStatus {

    SEND_SUCCESS(10,"调用渠道接口发送成功"),

    RECEIVE_SUCCESS(20,"用户收到短信(收到渠道短信回执，状态成功)"),

    RECEIVE_FAIL(30,"用户收不到短信(收到渠道短信回执，状态失败)"),

    SEND_FAIL(40,"调用渠道接口发送失败"),
    ;

    private Integer code;

    private String description;

    SmsStatus(Integer code, String description) {
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
}
