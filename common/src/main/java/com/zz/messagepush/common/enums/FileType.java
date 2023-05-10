package com.zz.messagepush.common.enums;

/**
 * @Description 文件类型
 * @Author 张卫刚
 * @Date Created on 2023/5/10
 */
public enum FileType {

    IMAGE("10", "image"),
    VOICE("20", "voice"),
    COMMON_FILE("30", "file"),
    VIDEO("40", "video"),
    ;

    private String code;

    private String dingDingName;

    FileType(String code, String dingDingName) {
        this.dingDingName = dingDingName;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getDingDingName() {
        return dingDingName;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String dingDingName) {
        this.dingDingName = dingDingName;
    }

    public static String dingDingNameByCode(String code) {
        for (FileType fileType : FileType.values()) {
            if (fileType.getCode().equals(code)) {
                return fileType.getDingDingName();
            }
        }
        return null;
    }
}
