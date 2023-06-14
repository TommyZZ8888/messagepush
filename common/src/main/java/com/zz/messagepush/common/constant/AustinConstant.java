package com.zz.messagepush.common.constant;


/**
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
public class AustinConstant {

    /**
     * 跨域地址端口
     */
    public static final String ORIGIN_VALUE = "http://localhost:3000";


    /**
     * businessId默认长度
     */
    public static final Integer BUSINESS_ID_LENGTH = 16;


    /**
     * 接口限制 最多的人数
     */
    public static final Integer BATCH_RECEIVER_SIZE = 100;


    /**
     * 消息发给全部人的标识
     * （企业微信 应用消息）
     * （钉钉自定义机器人）
     * (钉钉工作消息)
     */
    public static final String SEND_ALL = "@all";


}
