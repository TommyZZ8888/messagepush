package com.zz.messagepush.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.constant.SendAccountConstant;
import com.zz.messagepush.common.domain.dto.model.EnterpriseWeChatContentModel;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.common.enums.SendMessageType;
import com.zz.messagepush.handler.handler.BaseHandler;
import com.zz.messagepush.handler.handler.Handler;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.utils.AccountUtils;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpMessageServiceImpl;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.article.MpnewsArticle;
import me.chanjar.weixin.cp.bean.article.NewArticle;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

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

        // 通用配置
        WxCpMessage wxCpMessage = null;

        if (SendMessageType.TEXT.getCode().equals(contentModel.getSendType())) {
            wxCpMessage = WxCpMessage.TEXT().content(contentModel.getContent()).build();
        } else if (SendMessageType.IMAGE.getCode().equals(contentModel.getSendType())) {
            wxCpMessage = WxCpMessage.IMAGE().mediaId(contentModel.getMediaId()).build();
        } else if (SendMessageType.VOICE.getCode().equals(contentModel.getSendType())) {
            wxCpMessage = WxCpMessage.VOICE().mediaId(contentModel.getMediaId()).build();
        } else if (SendMessageType.VIDEO.getCode().equals(contentModel.getSendType())) {
            wxCpMessage = WxCpMessage.VIDEO().mediaId(contentModel.getMediaId()).description(contentModel.getDescription()).title(contentModel.getTitle()).build();
        } else if (SendMessageType.FILE.getCode().equals(contentModel.getSendType())) {
            wxCpMessage = WxCpMessage.FILE().mediaId(contentModel.getMediaId()).build();
        } else if (SendMessageType.TEXT_CARD.getCode().equals(contentModel.getSendType())) {
            wxCpMessage = WxCpMessage.TEXTCARD().url(contentModel.getUrl()).title(contentModel.getTitle()).description(contentModel.getDescription()).btnTxt(contentModel.getBtnTxt()).build();
        } else if (SendMessageType.NEWS.getCode().equals(contentModel.getSendType())) {
            List<NewArticle> newArticles = JSON.parseArray(contentModel.getArticles(), NewArticle.class);
            wxCpMessage = WxCpMessage.NEWS().articles(newArticles).build();
        } else if (SendMessageType.MP_NEWS.getCode().equals(contentModel.getSendType())) {
            List<MpnewsArticle> mpNewsArticles = JSON.parseArray(contentModel.getMpNewsArticle(), MpnewsArticle.class);
            wxCpMessage = WxCpMessage.MPNEWS().articles(mpNewsArticles).build();
        } else if (SendMessageType.MARKDOWN.getCode().equals(contentModel.getSendType())) {
            wxCpMessage = WxCpMessage.MARKDOWN().content(contentModel.getContent()).build();
        } else if (SendMessageType.MINI_PROGRAM_NOTICE.getCode().equals(contentModel.getSendType())) {
            Map contentItems = JSON.parseObject(contentModel.getContentItems(), Map.class);
            wxCpMessage = WxCpMessage.newMiniProgramNoticeBuilder().appId(contentModel.getAppId()).page(contentModel.getPage()).emphasisFirstItem(contentModel.getEmphasisFirstItem()).contentItems(contentItems).title(contentModel.getTitle()).description(contentModel.getDescription()).build();
        } else if (SendMessageType.TEMPLATE_CARD.getCode().equals(contentModel.getSendType())) {
            // WxJava 未支持
        }
        assert wxCpMessage != null;
        wxCpMessage.setAgentId(agentId);
        wxCpMessage.setToUser(userId);
        return wxCpMessage;
    }

    @Override
    public void recall(MessageTemplateEntity messageTemplate) {

    }
}
