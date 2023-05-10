package com.zz.messagepush.web.service.impl;

import cn.hutool.core.util.IdUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiMediaUploadRequest;
import com.dingtalk.api.response.OapiMediaUploadResponse;
import com.taobao.api.FileItem;
import com.zz.messagepush.common.constant.SendAccountConstant;
import com.zz.messagepush.common.enums.FileType;
import com.zz.messagepush.common.exception.ErrorException;
import com.zz.messagepush.common.utils.EnumUtil;
import com.zz.messagepush.web.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description MaterialServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/5/10
 */

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String DING_DING_URL = "https://oapi.dingtalk.com/media/upload";

    @Override
    public void dingDingMaterialUpload(MultipartFile file, String sendAccount, String fileType) {
        OapiMediaUploadResponse response;
        try {
            String accessToken = redisTemplate.opsForValue().get(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + sendAccount);
            DefaultDingTalkClient client = new DefaultDingTalkClient(DING_DING_URL);
            OapiMediaUploadRequest request = new OapiMediaUploadRequest();
            FileItem fileItem = new FileItem(new StringBuilder().append(IdUtil.fastUUID()).append(file.getOriginalFilename()).toString(), file.getInputStream());
            request.setMedia(fileItem);
            request.setType(FileType.dingDingNameByCode(fileType));
            response = client.execute(request);
            if (response.getErrcode() == 0) {
                return;
            }
            throw new ErrorException("未知错误");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
