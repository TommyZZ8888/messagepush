package com.zz.messagepush.handler.handler;


import com.zz.messagepush.common.domain.AnchorInfo;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.AnchorStateEnum;
import com.zz.messagepush.handler.flowcontrol.FlowControlParam;
import com.zz.messagepush.handler.flowcontrol.FlowControlService;
import com.zz.messagepush.support.utils.LogUtils;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @Description 发送各个渠道的handler
 * @Author 张卫刚
 * @Date Created on 2023/3/30
 */
public abstract class BaseHandler implements Handler {

    /**
     * 标识渠道的code
     * 子类初始化时指定
     */
    protected Integer channelCode;

    @Autowired
    private HandlerHolder handlerHolder;

    protected FlowControlParam flowControlParam;

    @Autowired
    private FlowControlService flowControlService;

    @Autowired
    private LogUtils logUtils;

    /**
     * 初始化渠道和handler的映射关系
     */
    @PostConstruct
    private void init() {
        handlerHolder.putHandler(channelCode, this);
    }

    /**
     * 消息处理
     *
     * @param taskInfo
     * @return
     */
    @Override
    public void doHandler(TaskInfo taskInfo) {
        if (handler(taskInfo)) {
            logUtils.print(AnchorInfo.builder().state(AnchorStateEnum.SEND_SUCCESS.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
            return;
        }
        logUtils.print(AnchorInfo.builder().state(AnchorStateEnum.SEND_FAIL.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
    }

    public void flowControl(TaskInfo taskInfo){
        if (flowControlParam!=null){
            flowControlService.flowControl(taskInfo,flowControlParam);
        }
    }

    /**
     * 统一处理的handler接口
     *
     * @param taskInfo
     */
    public abstract boolean handler(TaskInfo taskInfo);
}
