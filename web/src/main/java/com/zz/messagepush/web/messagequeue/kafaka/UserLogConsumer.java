package com.zz.messagepush.web.messagequeue.kafaka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/14
 */

@Component
@Slf4j
public class UserLogConsumer {

    @KafkaListener(topics = {"austin"}, groupId = "austinGroup1")
    public void consumer(ConsumerRecord<?, ?> consumerRecord) {
        Optional<?> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        log.info(">>>>>>>>>> record =" + kafkaMessage);
        if (kafkaMessage.isPresent()) {
            //得到Optional实例中的值
            Object message = kafkaMessage.get();
            System.err.println("消费消息:" + message);
        }
    }

}
