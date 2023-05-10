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
}
