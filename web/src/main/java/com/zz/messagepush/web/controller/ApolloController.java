package com.zz.messagepush.web.controller;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ObjectInputFilter;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/24
 */
@RestController
@RequestMapping("/apollo")
public class ApolloController {

@ApolloConfig("boss.austin")
private Config config;

@RequestMapping(value = "/test",method = RequestMethod.GET)
public String testApollo(){
    return config.getProperty("a","b");
}



}
