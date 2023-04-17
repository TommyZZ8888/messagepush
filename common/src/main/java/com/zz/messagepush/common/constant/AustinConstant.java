package com.zz.messagepush.common.constant;


/**
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
public class AustinConstant {

    /**
     * boolean转换
     */
    public final static Integer TRUE = 1;

    public final static Integer FALSE = 0;

    /**
     * apollo默认的值
     */
    public final static String APOLLO_DEFAULT_VALUE_JSON_OBJECT = "{}";
    public final static String APOLLO_DEFAULT_VALUE_JSON_ARRAY = "[]";

    /**
     * cron时间格式
     */
    public final static String CRON_FORMAT = "ss mm HH dd MM ? yyyy-yyyy";


    /**
     * businessId默认长度
     */
    public final static Integer BUSINESS_ID_LENGTH = 16;


    /**
     * 消息发给全部人的标识
     * （企业微信 应用消息）
     * （钉钉自定义机器人）
     */
    public static final String SEND_ALL = "@all";

    /**
     * 加密算法
     */
    public static final String HMAC_SHA256_ENCRYPTION_ALGO = "HmacSHA256";

    /**
     * 编码格式
     */
    public static final String CHARSET_NAME = "UTF-8";

}
