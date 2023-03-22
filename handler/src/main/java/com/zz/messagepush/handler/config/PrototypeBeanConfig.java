package com.zz.messagepush.handler.config;

import com.zz.messagepush.handler.pending.Task;
import com.zz.messagepush.handler.receiver.kafka.Receiver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/22
 */

@Configuration
public class PrototypeBeanConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Receiver receiver() {
        return new Receiver();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Task task(){
        return new Task();
    }
}
