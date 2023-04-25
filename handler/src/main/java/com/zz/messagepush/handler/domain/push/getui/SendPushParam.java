package com.zz.messagepush.handler.domain.push.getui;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Description 图送消息的param
 * @Author 张卫刚
 * @Date Created on 2023/4/23
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendPushParam {

    /**
     * requestId
     */
    @JSONField(name = "request_id")
    private String requestId;
    /**
     * settings
     */
    @JSONField(name = "settings")
    private SettingsVO settings;
    /**
     * audience
     */
    @JSONField(name = "audience")
    private AudienceVO audience;
    /**
     * pushMessage
     */
    @JSONField(name = "push_message")
    private PushMessageVO pushMessage;

    /**
     * SettingsVO  	推送条件设置
     */
    @NoArgsConstructor
    @Data
    public static class SettingsVO {
        /**
         * ttl
         */
        @JSONField(name = "ttl")
        private Integer ttl;
    }

    /**
     * AudienceVO  推送目标用户
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class AudienceVO {
        /**
         * cid
         */
        @JSONField(name = "cid")
        private Set<String> cid;
    }

    /**
     * PushMessageVO  个推推送消息参数
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class PushMessageVO {
        /**
         * notification
         */
        @JSONField(name = "notification")
        private NotificationVO notification;

        /**
         * NotificationVO
         */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        @Builder
        public static class NotificationVO {
            /**
             * title
             */
            @JSONField(name = "title")
            private String title;
            /**
             * body
             */
            @JSONField(name = "body")
            private String body;
            /**
             * clickType
             */
            @JSONField(name = "click_type")
            private String clickType;
            /**
             * url
             */
            @JSONField(name = "url")
            private String url;
        }
    }
}
