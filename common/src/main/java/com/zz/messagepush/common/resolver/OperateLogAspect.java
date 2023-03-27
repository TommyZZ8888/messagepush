package com.zz.messagepush.common.resolver;

import com.alibaba.fastjson.JSON;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

/**
 * 操作日志记录处理,对所有OperateLog注解的Controller进行操作日志监控
 */
@Aspect
@Component
@Slf4j
public class OperateLogAspect {

    @Autowired
//    private UserOperateLogService userOperateLogService;

//    @Pointcut("execution(* com.vren.*.module..*Controller.*(..))")
    public void logPointCut() {

    }

//    @AfterReturning(pointcut = "logPointCut()")
    public void doAfterReturning(JoinPoint joinPoint) {
        handleLog(joinPoint, null);
    }

//    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        handleLog(joinPoint, e);
    }

    protected void handleLog(final JoinPoint joinPoint, final Throwable e) {
//        try {
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            OperateLog operateLog = this.getAnnotationLog(joinPoint);
//            if (operateLog == null) return;
//            String className = joinPoint.getTarget().getClass().getName();
//            String methodName = joinPoint.getSignature().getName();
//            String operateMethod = className + "." + methodName;
//            Object[] args = joinPoint.getArgs();
//            StringBuilder sb = new StringBuilder();
//            for (Object obj : args) {
//                sb.append(obj.getClass().getSimpleName());
//                if (obj instanceof MultipartFile || obj instanceof HttpServletResponse) {
//                    continue;
//                }
//                sb.append("[");
//                sb.append(JSON.toJSONString(obj));
//                sb.append("]");
//            }
//            String params = sb.toString();
//            String failReason = null;
//            boolean result = true;
//            if (e != null) {
//                result = false;
//                StringWriter sw = new StringWriter();
//                PrintWriter pw = new PrintWriter(sw, true);
//                e.printStackTrace(pw);
//                failReason = sw.toString();
//                pw.flush();
//                pw.close();
//                sw.flush();
//                sw.close();
//            }
//            UserInfoEntity userInfoEntity = ThreadLocalUser.get();
//            //防止不需要登录的接口日志报错
//            if (userInfoEntity == null) {
//                userInfoEntity = new UserInfoEntity();
//            }
//            UserOperateLogEntity operateLogEntity =
//                    UserOperateLogEntity.builder()
//                            .userId(userInfoEntity.getUserId())
//                            .username(userInfoEntity.getUserName())
//                            .url(request.getRequestURI())
//                            .method(operateMethod)
//                            .param(params)
//                            .failReason(failReason)
//                            .result(result)
//                            .build();
//            ApiOperation apiOperation = this.getApiOperation(joinPoint);
//            if (apiOperation != null) {
//                operateLogEntity.setContent(apiOperation.value());
//            }
//            Api api = this.getApi(joinPoint);
//            if (api != null) {
//                String[] tags = api.tags();
//                operateLogEntity.setModule(StringUtils.join(tags, ","));
//            }
//            userOperateLogService.addLog(operateLogEntity);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

//    private OperateLog getAnnotationLog(JoinPoint joinPoint) throws Exception {
//        Signature signature = joinPoint.getSignature();
//        MethodSignature methodSignature = (MethodSignature) signature;
//        Method method = methodSignature.getMethod();
//        if (method != null) {
//            return AnnotationUtils.findAnnotation(method.getDeclaringClass(), OperateLog.class);
//        }
//        return null;
//    }

    /**
     * swagger API
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private Api getApi(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return AnnotationUtils.findAnnotation(method.getDeclaringClass(), Api.class);
        }
        return null;
    }

    /**
     * swagger ApiOperation
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private ApiOperation getApiOperation(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(ApiOperation.class);
        }
        return null;
    }
}
