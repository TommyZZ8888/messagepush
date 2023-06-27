package com.zz.messagepush.support.utils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Description AustinFileUtil
 * @Author 张卫刚
 * @Date Created on 2023/6/27
 */
public class AustinFileUtil {


    public static File getRemoteUrl2File(String path, String remoteUrl) {
        try {
            new File(path).mkdirs();
            URL url = new URL(remoteUrl);
            File file = new File(path, url.getPath());
            if (!file.exists()) {
                IOUtils.copy(url.openStream(), new FileOutputStream(file));
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
