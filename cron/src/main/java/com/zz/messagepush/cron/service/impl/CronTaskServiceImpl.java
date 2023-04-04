package com.zz.messagepush.cron.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.xxl.job.core.biz.model.ReturnT;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.common.exception.ErrorException;
import com.zz.messagepush.cron.constant.XxlJobConstant;
import com.zz.messagepush.cron.domain.dto.XxlJobInfoDTO;
import com.zz.messagepush.cron.domain.entity.XxlJobGroup;
import com.zz.messagepush.cron.service.CronTaskService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CronTaskServiceImpl implements CronTaskService {

    @Value("${xxl.job.admin.username}")
    private String xxlUserName;

    @Value("${xxl.job.admin.password}")
    private String xxlPassword;

    @Value("${xxl.job.admin.addresses}")
    private String xxlAddress;


    @Override
    public ResponseResult saveCronTask(XxlJobInfoDTO xxlJobInfoDTO) {
        Map<String, Object> map = JSONObject.parseObject(JSON.toJSONString(xxlJobInfoDTO), Map.class);
        String path;
        path = xxlJobInfoDTO.getId() == null ? xxlAddress + XxlJobConstant.INSERT_URL : xxlAddress + XxlJobConstant.UPDATE_URL;
        HttpResponse response;
        ReturnT returnT = null;
        try {
            response = HttpRequest.post(path).form(map).execute();
            returnT = JSONObject.parseObject(response.body(), ReturnT.class);
            if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
                if (path.contains(XxlJobConstant.INSERT_URL)) {
                    int taskId = Integer.parseInt(String.valueOf(JSON.parseObject(response.body()).get("content")));
                    return ResponseResult.success("success", taskId);
                } else if (path.contains(XxlJobConstant.UPDATE_URL)) {
                    return ResponseResult.success("success");
                }
            }
        } catch (Exception e) {
            log.error("CronTaskService#saveTask fail,e:{},param:{},response:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(xxlJobInfoDTO), JSON.toJSONString(returnT));
        }
        return ResponseResult.fail(RespStatusEnum.SERVICE_ERROR.getDescription());
    }

    @Override
    public ResponseResult deleteCronTask(Integer taskId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", taskId);
        String path = xxlAddress + XxlJobConstant.DELETE_URL;

        HttpResponse response;
        ReturnT returnT;

        response = HttpRequest.post(path).form(map).execute();
        returnT = JSONObject.parseObject(response.body(), ReturnT.class);

        if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
            return ResponseResult.success();
        }
        return ResponseResult.fail(RespStatusEnum.SERVICE_ERROR.getDescription());
    }

    @Override
    public ResponseResult startCronTask(Integer taskId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", taskId);
        String path = xxlAddress + XxlJobConstant.RUN_URL;

        HttpResponse response;
        ReturnT returnT;

        response = HttpRequest.post(path).form(map).execute();
        returnT = JSONObject.parseObject(response.body(), ReturnT.class);

        if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
            return ResponseResult.success();
        }
        return ResponseResult.fail(RespStatusEnum.SERVICE_ERROR.getDescription());
    }

    @Override
    public ResponseResult stopCronTask(Integer taskId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", taskId);
        String path = xxlAddress + XxlJobConstant.STOP_URL;

        HttpResponse response;
        ReturnT returnT;

        response = HttpRequest.post(path).form(map).cookie(getCookie()).execute();
        returnT = JSONObject.parseObject(response.body(), ReturnT.class);

        if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
            return ResponseResult.success();
        }
        return ResponseResult.fail(RespStatusEnum.SERVICE_ERROR.getDescription());
    }

    @Override
    public ResponseResult getGroupId(String appName, String title) {
        Map<String, Object> params = new HashMap<>();
        params.put("appname", appName);
        params.put("title", title);

        String path = xxlAddress + XxlJobConstant.JOB_GROUP_PAGE_LIST;
        HttpResponse response;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            if (response.isOk()) {
                Integer id = JSON.parseObject(response.body()).getJSONArray("data").getJSONObject(0).getInteger("id");
                return ResponseResult.success("success", id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.fail(RespStatusEnum.SERVICE_ERROR.getDescription());
    }

    @Override
    public ResponseResult createGroup(XxlJobGroup xxlJobGroup) {
        Map<String, Object> map = JSONObject.parseObject(JSON.toJSONString(xxlJobGroup), Map.class);
        String path = xxlAddress + XxlJobConstant.JOB_GROUP_INSERT_URL;
        HttpResponse response;
        ReturnT returnT;

        response = HttpRequest.post(path).form(map).cookie(getCookie()).execute();
        returnT = JSONObject.parseObject(response.body(), ReturnT.class);

        if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
            return ResponseResult.success();
        }
        return ResponseResult.fail(RespStatusEnum.SERVICE_ERROR.getDescription());
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
        paramsMap.put("randomCode", IdUtil.fastSimpleUUID());
        HttpResponse response = HttpRequest.post(path).form(paramsMap).execute();
        if (response.isOk()) {
            List<HttpCookie> cookies = response.getCookies();
            StringBuilder sb = new StringBuilder();
            for (HttpCookie cookie : cookies) {
                sb.append(cookie);
            }
            return sb.toString();
        }
        return null;
    }
}
