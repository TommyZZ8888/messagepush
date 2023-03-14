package com.zz.messagepush.web.messagequeue.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/14
 */

@Component
public class Productor {

    @Autowired
    private RabbitTemplate rabbitTemplate;


public void send(String msg){
    rabbitTemplate.convertAndSend("topic.exchange","topic.routing",msg);
}


}
