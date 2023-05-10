package com.zz.messagepush.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import com.zz.messagepush.common.constant.SendAccountConstant;
import com.zz.messagepush.common.domain.dto.model.EnterpriseWeChatContentModel;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.utils.AccountUtils;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpMessageServiceImpl;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/14
 */
public class EnterpriseWeChatHandler extends BaseHandler implements Handler {

    /**
     * 构建wxcpmessage时需要的常量信息
     */
    private static final String ALL = "@all";
    private static final String DELIMITER = "|";

    @Autowired
    private AccountUtils accountUtils;

    public EnterpriseWeChatHandler() {
        channelCode = ChannelType.ENTERPRISE_WE_CHAT.getCode();
    }


    @Override
    public boolean handler(TaskInfo taskInfo)  {
        WxCpDefaultConfigImpl account = accountUtils.getAccount(taskInfo.getSendAccount(), SendAccountConstant.ENTERPRISE_WECHAT_ACCOUNT_KEY, SendAccountConstant.ENTERPRISE_WECHAT_PREFIX, WxCpDefaultConfigImpl.class);
        WxCpMessageSendResult result = null;
        try {
            WxCpMessageServiceImpl wxCpMessageService = new WxCpMessageServiceImpl(initService(account));
            result = wxCpMessageService.send(buildWxCpMessage(taskInfo, account.getAgentId()));
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        buildAnchorState(result);
        return true;
    }


    private WxCpService initService(WxCpDefaultConfigImpl service) {
        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(service);
        return wxCpService;
    }

    /**
     * 打点相关的信息记录
     *
     * @param result
     */
    private void buildAnchorState(WxCpMessageSendResult result) {

    }

    private WxCpMessage buildWxCpMessage(TaskInfo taskInfo, Integer agentId) {
        String userId;
        if (ALL.equals(CollUtil.getFirst(taskInfo.getReceiver()))) {
            userId = CollUtil.getFirst(taskInfo.getReceiver());
        } else {
            userId = StringUtils.join(taskInfo.getReceiver(), DELIMITER);
        }

        EnterpriseWeChatContentModel contentModel = (EnterpriseWeChatContentModel) taskInfo.getContentModel();

        return WxCpMessage.TEXT()
                .agentId(agentId)
                .toUser(userId)
                .content(contentModel.getContent()).build();
    }

    @Override
    public void recall(MessageTemplateEntity messageTemplate) {

    }
}
