package com.zz.messagepush.web.aspect;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zz.messagepush.web.domain.vo.RequestLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description AustinAspect
 * @Author 张卫刚
 * @Date Created on 2023/6/27
 */
@Slf4j
@Component
@Aspect
public class AustinAspect {

    @Autowired
    private HttpServletRequest httpServletRequest;


    private final String requestIdKey = "request_unique_id";

    @Pointcut("@within(com.zz.messagepush.web.annotation.AustinAspect) || @annotation(com.zz.messagepush.web.annotation.AustinAspect)")
    public void executeService() {
    }

    @Before("executeService()")
    public void doBeforeAdvice(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        this.printRequestLog(signature, joinPoint.getArgs());
    }

    @AfterThrowing(value = "executeService()", throwing = "ex")
    public void doAfterThrowingAdvice(Throwable ex) {
        printExceptionLog(ex);
    }

    public void printRequestLog(MethodSignature methodSignature, Object[] argObs) {
        RequestLog logVo = new RequestLog();
        //设置请求唯一ID
        logVo.setId(IdUtil.fastUUID());
        httpServletRequest.setAttribute(requestIdKey, logVo.getId());
        logVo.setUri(httpServletRequest.getRequestURI());
        logVo.setMethod(httpServletRequest.getMethod());
        List<Object> args = Lists.newArrayList();
        //过滤掉一些不能转为json字符串的参数
        Arrays.stream(argObs).forEach(e -> {
            if (e instanceof MultipartFile || e instanceof HttpServletRequest
                    || e instanceof HttpServletResponse || e instanceof BindingResult) {
                return;
            }
            args.add(e);
        });
        logVo.setArgs(args.toArray());
        logVo.setProduct("austin");
        logVo.setPath(methodSignature.getDeclaringTypeName() + "." + methodSignature.getMethod().getName());
        logVo.setReferer(httpServletRequest.getHeader("referer"));
        logVo.setRemoteAddr(httpServletRequest.getRemoteAddr());
        logVo.setUserAgent(httpServletRequest.getHeader("user-agent"));
        log.info("austin-aspect-log,request:" + JSON.toJSONString(logVo));
    }


    public void printExceptionLog(Throwable ex) {
        JSONObject logVo = new JSONObject();
        logVo.put("id", httpServletRequest.getAttribute(requestIdKey));
        log.error("austin-aspect-log,exception:" + JSON.toJSONString(logVo), ex);
    }
}
