package com.zz.messagepush.support.utils;


import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @Description OssUtil
 * @Author 张卫刚
 * @Date Created on 2023/6/26
 */
public class OssUtil {

    public static void upLoad() {
        Configuration configuration = new Configuration(Region.autoRegion());
        configuration.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;

        UploadManager uploadManager = new UploadManager(configuration);
        //...生成上传凭证，然后准备上传
        String accessKey = "123";
        String secretKey = "123";
        String bucket = "austin3y";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\zhongfucheng\\Desktop\\1201.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = IdUtil.fastSimpleUUID();

        Auth auth = Auth.create(accessKey, secretKey);
        String uploadToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, accessKey, uploadToken);
            DefaultPutRet defaultPutRet = JSON.parseObject(response.toString(), DefaultPutRet.class);
            System.out.println(defaultPutRet.key);
            System.out.println(defaultPutRet.hash);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static String getFileUrl(String fileName) {
        String domainOdBucket = "http://devtools.qiniu.com/austin3y";
        String encodeFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        return String.format("%s/%s", domainOdBucket, encodeFileName);
    }
}
