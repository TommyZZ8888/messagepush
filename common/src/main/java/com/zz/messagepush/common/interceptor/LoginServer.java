package com.zz.messagepush.common.interceptor;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.zz.messagepush.common.exception.ErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class LoginServer {
    @Autowired
//    DiscoveryClient discoveryClient;

    public static final String AUTH_HEADER_KEY = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    public String checkToken(String token) {
        DecodedJWT decode = JWT.decode(token);
        String id = decode.getClaim("id").asString();
        Date expiresAt = decode.getExpiresAt();
        boolean expiration = expiresAt.before(new Date());
        if (expiration) {
            throw new ErrorException("登录已过期");
        }
        //调试用，打印出来
//        UserInfoEntity userInfo = new UserInfoEntity();
//        userInfo.setUserId(id);
        return "userInfo";
    }
}
