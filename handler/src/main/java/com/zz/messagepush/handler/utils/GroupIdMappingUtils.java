package com.zz.messagepush.handler.utils;

import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.common.enums.MessageType;
import com.zz.messagepush.common.utils.EnumUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description groupId标识每一个消费者组
 * groupId: channelType.code_en.messageType.code_en
 * @Author 张卫刚
 * @Date Created on 2023/3/22
 */
public class GroupIdMappingUtils {


    /**
     * 获取所有的groupIds
     * 不同的渠道不同的消息类型有自己的groupId
     *
     * @return
     */
    public static List<String> getAllGroupIds() {
        List<String> groupIds = new ArrayList<>();
        for (ChannelType channelType : ChannelType.values()) {
            for (MessageType messageType : MessageType.values()) {
                groupIds.add(channelType.getCode_en() + "." + messageType.getCode_en());
            }
        }
        return groupIds;
    }


    /**
     * 根据TaskInfo获取当前消息的groupId
     *
     * @param taskInfo
     * @return
     */
    public static String getGroupIdByTaskInfo(TaskInfo taskInfo) {
        String channelCodeEn = EnumUtil.getEnumByCode(ChannelType.class, taskInfo.getSendChannel()).getCode_en();
        String messageCodeEn = EnumUtil.getEnumByCode(MessageType.class, taskInfo.getMsgType()).getCode_en();
        return channelCodeEn + "." + messageCodeEn;
    }


}
