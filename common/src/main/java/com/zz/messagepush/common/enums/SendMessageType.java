package com.zz.messagepush.common.enums;

/**
 * @Description 微信下发消息类型枚举
 * @Author 张卫刚
 * @Date Created on 2023/4/14
 */
public enum SendMessageType {

    TEXT("10", "文本", "text", "text","text","text"),
    VOICE("20", "语音", null, "voice",null,null),
    VIDEO("30", "视频", null, null,null,null),
    NEWS("40", "图文", "feedCard", null,"news",null),
    TEXT_CARD("50", "文本卡片", null, null,null,null),
    FILE("60", "文件", null, "file","file",null),
    MINI_PROGRAM_NOTICE("70", "小程序通知", null, null,null,null),
    MARKDOWN("80", "markdown", "markdown", "markdown","markdown",null),
    TEMPLATE_CARD("90", "模板卡片", null, null,"template_card",null),
    IMAGE("100", "图片", null, "image","image","image"),
    LINK("110", "链接消息", "link", "link",null,null),
    ACTION_CARD("120", "跳转卡片消息", "actionCard", "action_card",null,"interactive"),
    OA("130", "OA消息", null, "oa",null,null),
    MP_NEWS("140", "图文消息(mpNews)", null, null,null,null),
    RICH_TEXT("150", "富文本", null, null,null,"post"),
    SHARE_CHAT("160", "群名片", null, null,null,"share_chat")

    ;


    public String code;

    public String description;

    public String dingDingRobotType;

    public String dingDingWorkType;

    public String enterpriseRobotType;

    public String feiShuRobotType;

    SendMessageType(String code, String description, String dingDingRobotType, String dingDingWorkType, String enterpriseRobotType,String feiShuRobotType) {
        this.code = code;
        this.description = description;
        this.dingDingRobotType = dingDingRobotType;
        this.dingDingWorkType = dingDingWorkType;
        this.enterpriseRobotType = enterpriseRobotType;
        this.feiShuRobotType = feiShuRobotType;
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

    public void setFeiShuRobotType(String feiShuRobotType) {
        this.feiShuRobotType = feiShuRobotType;
    }

    public String getFeiShuRobotType() {
        return feiShuRobotType;
    }


    public static String getEnterpriseWeChatRobotTypeByCode(String code) {
        for (SendMessageType sendMessageType : SendMessageType.values()) {
            if (code.equals(sendMessageType.getCode())) {
                return sendMessageType.getEnterpriseRobotType();
            }
        }
        return null;
    }

    public static String getFeiShuRobotTypeByCode(String code) {
        for (SendMessageType sendMessageType : SendMessageType.values()) {
            if (code.equals(sendMessageType.getCode())) {
                return sendMessageType.getFeiShuRobotType();
            }
        }
        return null;
    }
}
