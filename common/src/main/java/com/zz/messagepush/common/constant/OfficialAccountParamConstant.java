package com.zz.messagepush.common.constant;

/**
 * @Description 微信服务号额参数常量
 * @Author 张卫刚
 * @Date Created on 2023/6/15
 */
public class OfficialAccountParamConstant {

    public static final String SIGNATURE = "signature";
    public static final String ECHO_STR = "echostr";
    public static final String NONCE = "nonce";
    public static final String TIMESTAMP  ="timestamp";
    public static final String ENCRYPT_TYPE = "encrypt_type";
    public static final String RAW = "raw";
    public static final String AES = "aes";
    public static final String MSG_SIGNATURE = "msg_signature";

    /**
     * 处理器名
     */
    public static final String SCAN_HANDLER = "scanHandler";
    public static final String SUBSCRIBE_HANDLER = "subscribeHandler";
    public static final String UNSUBSCRIBE_HANDLER = "unSubscribeHandler";

    /**
     * 配置的beanName名
     */
    public static final String WE_CHAT_LOGIN_CONFIG = "weChatLoginConfig";


    /**
     * 二维码场景值的前缀
     */
    public static final String QR_CODE_SCENE_PREFIX = "qrscene_";
    /**
     * 关注后服务号的：文案
     */
    public static final String SUBSCRIBE_TIPS = "关注公众号：Java3y 回复 austin 可获取项目笔记哟！";

}
