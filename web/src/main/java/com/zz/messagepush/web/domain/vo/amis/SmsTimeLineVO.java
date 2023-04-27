package com.zz.messagepush.web.domain.vo.amis;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description SmsTimeLineVO
 * @Author 张卫刚
 * @Date Created on 2023/4/27
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsTimeLineVO {
    /**
     * items
     */
    private List<ItemsVO> items;

    /**
     * ItemsVO
     */
    @Data
    @Builder
    public static class ItemsVO {
        /**
         * 业务ID
         */
        public String businessId;
        /**
         * detail 发送内容
         */
        public String content;

        /**
         * 发送状态
         */
        public String sendType;

        /**
         * 回执状态
         */
        public String receiveType;

        /**
         * 回执报告
         */
        public String receiveContent;

        /**
         * 发送时间
         */
        public String sendTime;

        /**
         * 回执时间
         */
        public String receiveTime;


    }
}
