package com.zz.messagepush.handler.domain.sms;

import lombok.Builder;
import lombok.Data;

/**
 * @Description LinTongSendMessage
 * @Author 张卫刚
 * @Date Created on 2023/7/4
 */
@Data
@Builder
public class LinTongSendMessage {

    private String phone;

    private String content;
}
