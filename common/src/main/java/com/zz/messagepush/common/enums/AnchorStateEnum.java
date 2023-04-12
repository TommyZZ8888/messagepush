package com.zz.messagepush.common.enums;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/24
 */
public enum AnchorStateEnum {


    RECEIVE(10, "消息接收成功"),
    DISCARD(20, "消息丢弃"),
    CONTENT_DEDUPLICATION(30, "消息内容去重"),
    RULE_DEDUPLICATION(40, "消息频次去重"),
    WHITE_LIST(50, "白名单"),
    SEND_SUCCESS(60, "消息下发成功"),
    SEND_FAIL(70, "消息下发失败"),

    CLICK(0100, "消息被点击"),
    ;


    private Integer code;

    private String description;

    AnchorStateEnum(int code, String description) {
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


    /**
     * 通过code获取描述
     * @param code
     * @return
     */
    public static String getDescriptionByCode(Integer code){
        for (AnchorStateEnum anchorStateEnum : AnchorStateEnum.values()) {
            if (anchorStateEnum.getCode().equals(code)){
                return anchorStateEnum.getDescription();
            }
        }

        return "未知点位";
    }
}
