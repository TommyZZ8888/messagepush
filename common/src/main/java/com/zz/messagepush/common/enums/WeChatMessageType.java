package com.zz.messagepush.common.enums;

/**
 * @Description 微信下发消息类型枚举
 * @Author 张卫刚
 * @Date Created on 2023/4/14
 */
public enum WeChatMessageType {

    TEXT(10, "文本"),

    VOICE(20, "语音"),

    VIDEO(30, "视频"),

    NEWS(40, "图文"),

    TEXT_CARD(50, "文本卡片"),

    FILE(60, "文件"),

    MINI_PROGRAM_NOTICE(70, "小程序通知"),

    MARKDOWN(80, "markdown"),

    TEMPLATE_CARD(90, "模板卡片"),

    IMAGE(100, "图片"),
    ;

    public Integer code;

    public String description;

    WeChatMessageType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
