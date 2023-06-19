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

    private String name;

    FileType(String code, String name) {
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getNameByCode(String code) {
        for (FileType fileType : FileType.values()) {
            if (fileType.getCode().equals(code)) {
                return fileType.getName();
            }
        }
        return null;
    }
}
