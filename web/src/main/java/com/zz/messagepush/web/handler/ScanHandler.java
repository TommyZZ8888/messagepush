package com.zz.messagepush.web.handler;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.common.constant.OfficialAccountParamConstant;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description ScanHandler
 * @Author 张卫刚
 * @Date Created on 2023/6/16
 */
@Component("scanHandler")
@Slf4j
public class ScanHandler implements WxMpMessageHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        String content = DateUtil.now() + StrUtil.COLON + wxMessage.getFromUser() + StrUtil.COLON + OfficialAccountParamConstant.SCAN_HANDLER;
        try {
//            WxMpUser user = wxMpService.getUserService().userInfo(wxMessage.getFromUser());
//            redisTemplate.opsForValue().set(wxMessage.getEventKey(), JSON.toJSONString(user), 30, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("ScanHandler#handle fail:{}", Throwables.getStackTraceAsString(e));
        }
        return WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .content(content).build();
    }
}
