package com.zz.messagepush.common.constant;

/**
 * @Description 发送账号的常量信息汇总
 * 读取apollo的key和前缀
 * @Author 张卫刚
 * @Date Created on 2023/4/20
 */
public class SendAccountConstant {


    public static final Integer START = 10;
    public static final Integer STEP = 10;

    /**
     * 钉钉自定义机器人
     */
    public static final String DING_DING_ROBOT_ACCOUNT_KEY = "dingDingRobotAccount";
    public static final String DING_DING_ROBOT_PREFIX = "ding_ding_robot_";

    /**
     * 钉钉工作通知
     */
    public final static String DING_DING_WORK_NOTICE_ACCOUNT_KEY = "dingDingNoticeAccount";
    public final static String DING_DING_WORK_NOTICE_PREFIX = "ding_ding_notice_account_";

    /**
     * 邮件
     */
    public static final String EMAIL_ACCOUNT_KEY = "emailAccount";
    public static final String EMAIL_PREFIX = "email_";

    /**
     * 企业微信
     */
    public static final String ENTERPRISE_WECHAT_ACCOUNT_KEY = "enterpriseWeChatAccount";
    public static final String ENTERPRISE_WECHAT_PREFIX = "enterprise_wechat_";

    /**
     * 微信服务号
     */
    public static final String WECHAT_OFFICIAL_ACCOUNT_KEY = "officialAccount";
    public static final String WECHAT_OFFICIAL_PREFIX = "official_";

    /**
     * 微信小程序
     */
    public static final String WECHAT_MINI_PROGRAM_ACCOUNT_KEY = "miniProgramAccount";
    public static final String WECHAT_MINI_PROGRAM_PREFIX = "mini_program_";


    /**
     * 个推
     */
    public static final String GE_TUI_ACCOUNT_KEY = "geTuiAccount";
    public static final String GE_TUI_ACCOUNT_PREFIX = "ge_tui_account_";
    public static final String GE_TUI_ACCESS_TOKEN_PREFIX = "ge_tui_access_token_";

}
