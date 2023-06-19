package com.zz.messagepush.web.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Description 素材接口
 * @Author 张卫刚
 * @Date Created on 2023/5/10
 */
public interface MaterialService {

    /**
     * 钉钉素材上传
     *
     * @param file
     * @param sendAccount
     * @param fileType
     */
    void dingDingMaterialUpload(MultipartFile file, String sendAccount, String fileType);

    /**
     * 企业微信（机器人）素材上传
     * @param file
     * @param sendAccount
     * @param fileType
     * @return
     */
    void enterpriseWeChatRootMaterialUpload(MultipartFile file, String sendAccount, String fileType);

    /**
     * 企业微信（应用消息）素材上传
     * @param file
     * @param sendAccount
     * @param fileType
     * @return
     */
    void enterpriseWeChatMaterialUpload(MultipartFile file, String sendAccount, String fileType);
}
