package com.zz.messagepush.handler.anno;

import com.zz.messagepush.handler.enums.RateLimitStrategy;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @Description LocalRateLimit
 * @Author 张卫刚
 * @Date Created on 2023/6/6
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
@Documented
public @interface LocalRateLimit {

RateLimitStrategy rateLimitStrategy() default RateLimitStrategy.REQUEST_RATE_LIMIT;
}
