package com.zz.messagepush.handler.handler;

import com.zz.messagepush.common.domain.AnchorInfo;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.AnchorStateEnum;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.support.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
public interface Handler {


    /**
     * 处理器
     * @param taskInfo
     */
    void doHandler(TaskInfo taskInfo);

}
