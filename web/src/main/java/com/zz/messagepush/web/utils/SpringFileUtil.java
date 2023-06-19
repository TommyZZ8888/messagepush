package com.zz.messagepush.web.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Description SpringFileUtil
 * @Author 张卫刚
 * @Date Created on 2023/6/19
 */
public class SpringFileUtil {

    public static File getFile(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        assert originalFilename != null;
        File file = new File(originalFilename);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            byte[] bytes = multipartFile.getBytes();
            for (int i = 0; i < bytes.length; i++) {
                outputStream.write(bytes[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return file;
    }
}
