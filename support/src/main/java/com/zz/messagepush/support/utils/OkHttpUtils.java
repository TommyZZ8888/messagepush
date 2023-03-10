package com.zz.messagepush.support.utils;

import cn.hutool.core.map.MapUtil;
import com.google.common.base.Throwables;
import io.swagger.models.Xml;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Author 张卫刚
 * @Date Created on 2023/3/10
 */

@Component
@Slf4j
public class OkHttpUtils {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    @Autowired
    private OkHttpClient okHttpClient;


    public String doGet(String url) {
        return doGet(url, null, null);
    }

    public String doGet(String url, Map<String, String> params) {
        return doGet(url, params, null);
    }

    public String doGetWithHeaders(String url, Map<String, String> headers) {
        return doGet(url, null, headers);
    }

    /**
     * get 请求
     *
     * @param url     请求的地址
     * @param params  请求参数 map
     * @param headers 请求的头字段 {k1,v1,k2,v2,...}
     * @return string
     */
    public String doGet(String url, Map<String, String> params, Map<String, String> headers) {
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.keySet().size() > 0) {
            boolean firstFlag = true;
            for (String key : params.keySet()) {
                if (firstFlag) {
                    sb.append("?").append(key).append("=").append(params.get(key));
                    firstFlag = false;
                } else {
                    sb.append("&").append(key).append("=").append(params.get(key));
                }
            }
        }
        Request.Builder builder = getBuilderWithHeaders(headers);
        Request request = builder.url(sb.toString()).build();

        log.info("do get request and url[{}]", sb.toString());
        return execute(request);
    }

    /**
     * post 请求
     *
     * @param url     url 请求的地址
     * @param params  请求参数 map
     * @param headers 请求的头字段 {k1,v1,k2,v2,...}
     * @return string
     */
    public String doPost(String url, Map<String, String> params, Map<String, String> headers) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request.Builder builderWithHeaders = getBuilderWithHeaders(headers);
        Request request = builderWithHeaders.url(url).post(builder.build()).build();
        log.info("do post request and url[{}]", url);
        return execute(request);
    }


    /**
     * post 请求，请求数据为json的字符串
     *
     * @param url  请求url地址
     * @param json 请求数据，json字符串
     * @return string
     */
    public String doPostJson(String url, String json) {
        log.info("do post request and url[{}]", url);
        return executePost(url, json, JSON, null);
    }


    public String doPostJsonWithHeaders(String url, String json, Map<String, String> headers) {
        log.info("do post request and url[{}]", url);
        return executePost(url, json, JSON, headers);
    }

    /**
     * post 请求，请求数据为xml的字符串
     *
     * @param url 请求url地址
     * @param xml 请求数据，xml字符串
     * @return string
     */
    public String doPostXml(String url, String xml) {
        log.info("do post request and url[{}]", url);
        return executePost(url, xml, XML, null);
    }


    public Request.Builder getBuilderWithHeaders(Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        if (!MapUtil.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return builder;
    }


    public String executePost(String url, String data, MediaType contentType, Map<String, String> headers) {
        RequestBody requestBody = RequestBody.create(data.getBytes(StandardCharsets.UTF_8), contentType);
        Request.Builder builder = getBuilderWithHeaders(headers);
        Request request = builder.url(url).post(requestBody).build();
        return execute(request);
    }


    public String execute(Request request) {
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            log.error(Throwables.getStackTraceAsString(e));
            throw new RuntimeException(e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return "";
    }
}
