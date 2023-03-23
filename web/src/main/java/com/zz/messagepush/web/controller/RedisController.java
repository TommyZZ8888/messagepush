package com.zz.messagepush.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/23
 */

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public void testRedis() {
        redisTemplate.opsForValue().set("austin", "austin");
    }

}
