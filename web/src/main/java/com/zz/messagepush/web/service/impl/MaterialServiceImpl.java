package com.zz.messagepush.web.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiMediaUploadRequest;
import com.dingtalk.api.response.OapiMediaUploadResponse;
import com.taobao.api.FileItem;
import com.zz.messagepush.common.constant.CommonConstant;
import com.zz.messagepush.common.constant.SendAccountConstant;
import com.zz.messagepush.common.domain.dto.account.EnterpriseWeChatRobotAccount;
import com.zz.messagepush.common.enums.FileType;
import com.zz.messagepush.common.exception.ErrorException;
import com.zz.messagepush.common.utils.EnumUtil;
import com.zz.messagepush.handler.domain.wechat.robot.EnterpriseWeChatRobotResult;
import com.zz.messagepush.support.utils.AccountUtils;
import com.zz.messagepush.web.service.MaterialService;
import com.zz.messagepush.web.utils.SpringFileUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description MaterialServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/5/10
 */
@Slf4j
@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AccountUtils accountUtils;

    private static final String DING_DING_URL = "https://oapi.dingtalk.com/media/upload";

    private static final String ENTERPRISE_WE_CHAT_ROBOT_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/upload_media?key=<KEY>&type=<TYPE>";


    @Override
    public void dingDingMaterialUpload(MultipartFile file, String sendAccount, String fileType) {
        OapiMediaUploadResponse response;
        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient(DING_DING_URL);
            OapiMediaUploadRequest request = new OapiMediaUploadRequest();
            FileItem fileItem = new FileItem(IdUtil.fastUUID() + file.getOriginalFilename(), file.getInputStream());
            request.setMedia(fileItem);
            request.setType(FileType.getNameByCode(fileType));
            response = client.execute(request);
            if (response.getErrcode() == 0) {
                return;
            }
            throw new ErrorException("未知错误");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enterpriseWeChatRootMaterialUpload(MultipartFile multipartFile, String sendAccount, String fileType) {
        try {
            EnterpriseWeChatRobotAccount weChatRobotAccount = accountUtils.getAccountById(Integer.valueOf(sendAccount), EnterpriseWeChatRobotAccount.class);
            String key = weChatRobotAccount.getWebhook().substring(weChatRobotAccount.getWebhook().indexOf(CommonConstant.EQUAL_STRING) + 1);
            String url = ENTERPRISE_WE_CHAT_ROBOT_URL.replace("<KEY>", key).replace("<TYPE>", "file");
            String response = HttpRequest.post(url)
                    .form(IdUtil.fastSimpleUUID(), SpringFileUtil.getFile(multipartFile))
                    .execute().body();
            EnterpriseWeChatRobotResult result = JSON.parseObject(response, EnterpriseWeChatRobotResult.class);
            if (result.getErrcode() == 0) {
               throw new ErrorException("enterpriseWeChatRootMaterialUpload fail");
            }
            log.error("MaterialService#enterpriseWeChatRootMaterialUpload fail:{}", result.getErrmsg());
        } catch (Exception e) {
            log.error("MaterialService#enterpriseWeChatRootMaterialUpload fail:{}", Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    public void enterpriseWeChatMaterialUpload(MultipartFile multipartFile, String sendAccount, String fileType) {
        try {
            WxCpDefaultConfigImpl accountConfig = accountUtils.getAccountById(Integer.valueOf(sendAccount), WxCpDefaultConfigImpl.class);
            WxCpServiceImpl wxCpService = new WxCpServiceImpl();
            wxCpService.setWxCpConfigStorage(accountConfig);
            WxMediaUploadResult result = wxCpService.getMediaService().upload(FileType.getNameByCode(fileType), SpringFileUtil.getFile(multipartFile));
            log.error("MaterialService#enterpriseWeChatMaterialUpload fail:{}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("MaterialService#enterpriseWeChatMaterialUpload fail:{}", Throwables.getStackTraceAsString(e));
        }
        throw new ErrorException("enterpriseWeChatMaterialUpload fail");
    }
}
