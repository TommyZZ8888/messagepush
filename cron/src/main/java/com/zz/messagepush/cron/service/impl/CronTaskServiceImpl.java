package com.zz.messagepush.cron.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.exception.ErrorException;
import com.zz.messagepush.cron.constant.XxlJobConstant;
import com.zz.messagepush.cron.domain.dto.XxlJobInfoDTO;
import com.zz.messagepush.cron.service.CronTaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/31
 */
@Service
public class CronTaskServiceImpl implements CronTaskService {

    @Value("${xxl.admin.username}")
    private String xxlUserName;

    @Value("${ssl.admin.password}")
    private String xxlPassword;

    @Value("${xxl.admin.address}")
    private String xxlAddress;


    @Override
    public void saveCronTask(XxlJobInfoDTO xxlJobInfoDTO) {
        Map<String, Object> map = JSONObject.parseObject(JSON.toJSONString(xxlJobInfoDTO), Map.class);
        String path;
        if (xxlJobInfoDTO.getId() == null) {
            path = xxlAddress + XxlJobConstant.INSERT_URL;
        } else {
            path = xxlAddress + XxlJobConstant.UPDATE_URL;
        }
        HttpResponse response = HttpRequest.post(path).form(map).execute();
        if (!response.isOk()) {
            throw new ErrorException("保存失败");
        }
    }

    @Override
    public void deleteCronTask(Integer taskId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id",taskId);
        String path = xxlAddress + XxlJobConstant.DELETE_URL;

        HttpResponse response = HttpRequest.post(path).form(map).execute();
        if (!response.isOk()) {
            throw new ErrorException("保存失败");
        }
    }

    @Override
    public void startCronTask(Integer taskId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id",taskId);
        String path = xxlAddress + XxlJobConstant.RUN_URL;

        HttpResponse response = HttpRequest.post(path).form(map).execute();
        if (!response.isOk()) {
            throw new ErrorException("保存失败");
        }
    }

    @Override
    public void stopCronTask(Integer taskId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id",taskId);
        String path = xxlAddress + XxlJobConstant.STOP_URL;

        HttpResponse response = HttpRequest.post(path).form(map).execute();
        if (!response.isOk()) {
            throw new ErrorException("保存失败");
        }
    }


    /**
     * 获取xxl-job  cookie
     *
     * @return
     */
    private String getCookie() {
        String path = xxlAddress + XxlJobConstant.LOGIN_URL;
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("username", xxlUserName);
        paramsMap.put("password", xxlPassword);

        HttpResponse response = HttpRequest.post(path).form(paramsMap).execute();
        if (response.isOk()) {
            List<HttpCookie> cookies = response.getCookies();
            StringBuilder sb = new StringBuilder();
            for (HttpCookie cookie : cookies) {
                sb.append(cookies);
            }
            return sb.toString();
        }
        return null;
    }
}
