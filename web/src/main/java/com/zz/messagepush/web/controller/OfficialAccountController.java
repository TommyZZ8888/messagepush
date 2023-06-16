package com.zz.messagepush.web.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.constant.CommonConstant;
import com.zz.messagepush.common.constant.OfficialAccountParamConstant;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.domain.dto.account.WeChatOfficialAccount;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.common.utils.ApplicationContextUtil;
import com.zz.messagepush.support.utils.WxServiceUtil;
import com.zz.messagepush.web.config.WeChatLoginAccountConfig;
import com.zz.messagepush.web.domain.vo.amis.CommonAmisVo;
import com.zz.messagepush.web.utils.Convert4Amis;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description OfficialAccountController
 * @Author 张卫刚
 * @Date Created on 2023/6/14
 */

@Api("微信服务号")
@RestController
@RequestMapping("/officialAccount")
@Slf4j
public class OfficialAccountController {

    @Autowired
    private WxServiceUtil wxServiceUtil;

    @Autowired
    private WeChatLoginAccountConfig configService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ApplicationContextUtil applicationContext;


    @RequestMapping(value = "/templateList", method = RequestMethod.GET)
    @ApiOperation("根据模板id获取模板列表")
    public ResponseResult<List<CommonAmisVo>> queryList(Long id) throws WxErrorException {
        List<CommonAmisVo> list = new ArrayList<>();
        WxMpService wxMpService = wxServiceUtil.getOfficialAccountServiceMap().get(id);
        List<WxMpTemplate> allPrivateTemplate = wxMpService.getTemplateMsgService().getAllPrivateTemplate();
        for (WxMpTemplate wxMpTemplate : allPrivateTemplate) {
            CommonAmisVo commonAmisVo = CommonAmisVo.builder().label(wxMpTemplate.getTitle()).value(wxMpTemplate.getTemplateId()).build();
            list.add(commonAmisVo);
        }
        return ResponseResult.success("ok", list);
    }


    @RequestMapping(value = "/detailTemplate", method = RequestMethod.GET)
    @ApiOperation("根据模板id和账号id获取模板列表")
    public ResponseResult<List<CommonAmisVo>> queryDetailList(Long id, String wxTemplateId) throws WxErrorException {
        List<CommonAmisVo> list = new ArrayList<>();
        WxMpService wxMpService = wxServiceUtil.getOfficialAccountServiceMap().get(id);
        List<WxMpTemplate> allPrivateTemplate = wxMpService.getTemplateMsgService().getAllPrivateTemplate();
        for (WxMpTemplate wxMpTemplate : allPrivateTemplate) {
            if (wxTemplateId.equals(wxMpTemplate.getTemplateId())) {
                String[] data = wxMpTemplate.getContent().split(StrUtil.LF);
                for (String datum : data) {
                    String name = datum.substring(datum.indexOf("{{") + 2, datum.indexOf("."));
                    CommonAmisVo commonAmisVo = CommonAmisVo.builder()
                            .name(name)
                            .type("text")
                            .required(true)
                            .build();
                    if (datum.contains("first")) {
                        commonAmisVo.setLabel("名字");
                    } else if (datum.contains("remark")) {
                        commonAmisVo.setLabel("备注");
                    } else {
                        commonAmisVo.setLabel(datum.split("：")[0]);

                    }
                    list.add(commonAmisVo);
                }
            }
        }
        return ResponseResult.success("ok", list);
    }


    @RequestMapping(value = "/receipt", produces = {CommonConstant.CONTENT_TYPE_XML})
    public String receiptMessage(HttpServletRequest request) {
        try {
            WeChatLoginAccountConfig configService = ApplicationContextUtil.getBean(OfficialAccountParamConstant.WE_CHAT_LOGIN_CONFIG, WeChatLoginAccountConfig.class);
            if (configService == null) {
                return RespStatusEnum.NO_LOGIN.getDescription();
            }
            WxMpService wxMpService = configService.getWxOfficialAccountLoginService();

            String echoStr = request.getParameter(OfficialAccountParamConstant.ECHO_STR);
            String signature = request.getParameter(OfficialAccountParamConstant.SIGNATURE);
            String nonce = request.getParameter(OfficialAccountParamConstant.NONCE);
            String timestamp = request.getParameter(OfficialAccountParamConstant.TIMESTAMP);

            // echoStr!=null，说明只是微信调试的请求
            if (StrUtil.isNotBlank(echoStr)) {
                return echoStr;
            }

            if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
                return RespStatusEnum.CLIENT_BAD_PARAMETERS.getDescription();
            }

            String encryptType = StrUtil.isBlank(request.getParameter(OfficialAccountParamConstant.ENCRYPT_TYPE)) ? OfficialAccountParamConstant.RAW : request.getParameter(OfficialAccountParamConstant.ENCRYPT_TYPE);

            if (OfficialAccountParamConstant.RAW.equals(encryptType)) {
                WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
                log.info("raw inMessage:{}", JSON.toJSONString(inMessage));
                WxMpXmlOutMessage outMessage = configService.getWxMpMessageRouter().route(inMessage);
                return outMessage.toXml();
            } else if (OfficialAccountParamConstant.AES.equals(encryptType)) {
                String msgSignature = request.getParameter(OfficialAccountParamConstant.MSG_SIGNATURE);
                WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), configService.getWxMpDefaultConfig(), timestamp, nonce, msgSignature);
                log.info("aes inMessage:{}", JSON.toJSONString(inMessage));
                WxMpXmlOutMessage outMessage = configService.getWxMpMessageRouter().route(inMessage);
                return outMessage.toEncryptedXml(configService.getWxMpDefaultConfig());
            }

        } catch (Exception e) {
            log.error("OfficialAccountController#receiptMessage fail:{}", Throwables.getStackTraceAsString(e));
            return RespStatusEnum.SERVICE_ERROR.getDescription();
        }
        return RespStatusEnum.SUCCESS.getDescription();
    }


    @PostMapping("/qrCode")
    @ApiOperation("/生成 服务号 二维码")
    public CommonAmisVo getQrCode() throws WxErrorException {
        WeChatLoginAccountConfig configService = ApplicationContextUtil.getBean(OfficialAccountParamConstant.WE_CHAT_LOGIN_CONFIG, WeChatLoginAccountConfig.class);
        if (configService == null) {
            return null;
        }
        String id = IdUtil.getSnowflake().nextIdStr();

        // 获取服务号二维码
        WxMpService wxMpService = configService.getWxOfficialAccountLoginService();
        WxMpQrCodeTicket ticket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(id, 2592000);
        String url = wxMpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
        // 存入Redis做校验
        return Convert4Amis.getWxMpQrCode(url, id);
    }


    /**
     * 临时给微信服务号登录使用（给前端轮询检查是否已登录），正常消息推送平台不会有此接口
     * @return
     */
    @RequestMapping("/check/login")
    @ApiOperation("/检查是否已经登录")
    public ResponseResult<Boolean> checkLogin(String sceneId) {
        try {
            String userInfo = redisTemplate.opsForValue().get(sceneId);
            if (StrUtil.isBlank(userInfo)) {
                return ResponseResult.success(RespStatusEnum.NO_LOGIN.getDescription());
            }
            return ResponseResult.success(JSON.parseObject(userInfo, WxMpUser.class).toString());
        } catch (Exception e) {
            log.error("OfficialAccountController#checkLogin fail:{}", Throwables.getStackTraceAsString(e));
            return null;
        }
    }
}
