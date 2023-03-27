package com.zz.messagepush.common.interceptor;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 登录拦截器
 */
@Component
@Slf4j
public class BaseLoginCheckInterceptor implements HandlerInterceptor {




    @Autowired(required = false)
    private CustomerLoginCheckInterceptor customerLoginCheckInterceptor;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HashMap<String, String> headers = this.getHeaders(request);
//        InheritableThreadLocalHeader.set(headers);
        boolean isHandlerMethod = handler instanceof HandlerMethod;
        if (!isHandlerMethod) {
            return true;
        }
        //放行有NoNeedLogin注解方法
//        boolean isNoNeedLogin = ((HandlerMethod) handler).getMethodAnnotation(NoNeedLogin.class) != null;
//        if (isNoNeedLogin) {
//            return true;
//        }

        //放行的Uri前缀
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String target = uri.replaceFirst(contextPath, "");
        if (target.startsWith("/swagger")) {
            return true;
        }
        String token = request.getHeader(LoginServer.AUTH_HEADER_KEY);
//        if (StringUtils.isBlank(token)) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return false;
//        }
//        if (!token.startsWith(LoginServer.TOKEN_PREFIX)) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return false;
//        }
//        UserInfoEntity userInfoEntity;
//        try {
//            final String authToken = token.replace(LoginServer.TOKEN_PREFIX, "");
//            userInfoEntity = loginServer.checkToken(authToken);
//        } catch (Exception e) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            this.outputResult(response, "UNAUTHORIZED " + e.getMessage());
//            return false;
//        }
//        UserInfoEntity userInfo = null;
//        try {
//            userInfo = userService.getUserInfoByID(userInfoEntity.getUserId());
//        } catch (FeignException e) {
//            if (e.status() == HttpServletResponse.SC_UNAUTHORIZED) {
//                this.outputResult(response, "UNAUTHORIZED");
//                return false;
//            }
//        }
//        if (userInfo == null) {
//            this.outputResult(response, "用户不存在");
//            return false;
//        }
//        List<ModuleListEntity> roleLinkModuleList = roleService.getRoleLinkModuleList(userInfo.getRoleId());
//        userInfo.setModuleList(roleLinkModuleList);
//        userInfo.setModuleCodeList(roleLinkModuleList.stream().map(ModuleListEntity::getModuleCode).collect(Collectors.toList()));
//        ThreadLocalUser.set(userInfo);
//        //根据不同模块的不同逻辑 自定义拦截器
//        if (customerLoginCheckInterceptor != null) {
//            try {
//                customerLoginCheckInterceptor.interceptor();
//            } catch (ErrorException e) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                this.outputResult(response, e.getMessage());
//                return false;
//            }
//        }
//        for (MethodParameter methodParameter : ((HandlerMethod) handler).getMethodParameters()) {
//            if (methodParameter.hasParameterAnnotation(ConversionNumber.class)) {
//                request.getParameterMap().get(methodParameter.getParameterName());
//            }
//        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //请求完成 需要清空threadLocal防止内存溢出
//        ThreadLocalUser.clear();
//        InheritableThreadLocalHeader.clear();
    }

    /**
     * 输出信息
     *
     * @param response
     * @param msg
     * @throws IOException
     */
    protected void outputResult(HttpServletResponse response, String msg) throws IOException {
//        ResponseResult<Object> result = ResponseResult.error(msg);
//        String resultMsg = JSONObject.toJSONString(result);
//        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write(resultMsg);
//        response.flushBuffer();
    }

    protected HashMap<String, String> getHeaders(HttpServletRequest request) {
        HashMap<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
}
