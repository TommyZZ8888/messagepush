package com.zz.messagepush.common.enums;

/**
 * @Description 微信下发消息类型枚举
 * @Author 张卫刚
 * @Date Created on 2023/4/14
 */
public enum SendMessageType {

    TEXT("10", "文本", "text", "text", "text"),

    VOICE("20", "语音", null, "voice", null),

    VIDEO("30", "视频", null, null, null),

    NEWS("40", "图文", "feedCard", null, "news"),

    TEXT_CARD("50", "文本卡片", null, null, null),

    FILE("60", "文件", null, "file", "file"),

    MINI_PROGRAM_NOTICE("70", "小程序通知", null, null, null),

    MARKDOWN("80", "markdown", "markdown", "markdown", "markdown"),

    TEMPLATE_CARD("90", "模板卡片", null, null, "template_card"),

    IMAGE("100", "图片", null, "image", "image"),

    LINK("110", "链接消息", "link", "link", null),

    ACTION_CARD("120", "跳转卡片消息", "actionCard", "action_card", null),

    OA("130", "OA消息", null, "oa", null),
    MP_NEWS("140", "图文消息（mpnews）", null, null, null),
    ;

    public String code;

    public String description;

    public String dingDingRobotType;

    public String dingDingWorkType;

    public String enterpriseRobotType;

    SendMessageType(String code, String description, String dingDingRobotType, String dingDingWorkType, String enterpriseRobotType) {
        this.code = code;
        this.description = description;
        this.dingDingRobotType = dingDingRobotType;
        this.dingDingWorkType = dingDingWorkType;
        this.enterpriseRobotType = enterpriseRobotType;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDingDingWorkType(String dingDingWorkType) {
        this.dingDingWorkType = dingDingWorkType;
    }

    public String getDingDingWorkType() {
        return dingDingWorkType;
    }

    public void setDingDingRobotType(String dingDingRobotType) {
        this.dingDingRobotType = dingDingRobotType;
    }

    public String getDingDingRobotType() {
        return dingDingRobotType;
    }

    public void setEnterpriseRobotType(String enterpriseRobotType) {
        this.enterpriseRobotType = enterpriseRobotType;
    }

    public String getEnterpriseRobotType() {
        return enterpriseRobotType;
    }


    public static String getEnterpriseWeChatRobotTypeByCode(String code) {
        for (SendMessageType sendMessageType : SendMessageType.values()) {
            if (code.equals(sendMessageType.getCode())) {
                return sendMessageType.getEnterpriseRobotType();
            }
        }
        return null;
    }
}
