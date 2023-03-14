package com.zz.messagepush.web.messagequeue.rabbitmq;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/14
 */
@Component
@RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "topic",autoDelete = "false"),
exchange = @Exchange(value = "topic.exchange",type = ExchangeTypes.TOPIC),
key = "topic.routing")})
public class Consumer {

@RabbitHandler
public void receive(String msg){
    System.out.println(msg);
}

}
