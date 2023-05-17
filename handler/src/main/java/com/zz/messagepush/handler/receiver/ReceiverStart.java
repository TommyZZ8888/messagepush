package com.zz.messagepush.handler.receiver;

import com.zz.messagepush.handler.receiver.kafka.Receiver;
import com.zz.messagepush.handler.utils.GroupIdMappingUtils;
import com.zz.messagepush.support.constant.MessageQueuePipeline;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * @Description 启动消费者
 * @Author 张卫刚
 * @Date Created on 2023/3/22
 */


@Service
@ConditionalOnProperty(name = "austin-mq-pipeline",havingValue = MessageQueuePipeline.KAFKA)
public class ReceiverStart {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ConsumerFactory consumerFactory;

    /**
     * receiver的消费方法常量
     */
    private static final String RECEIVER_METHOD_NAME = "Receiver.consumer";

    /**
     * 获取到所有的groupId
     */
    private static final List<String> GROUP_IDS = GroupIdMappingUtils.getAllGroupIds();

    /**
     * 下标（用于迭代groupId的位置）
     */
    private static Integer index = 0;


    /**
     * 为每个渠道不同的消息类型   创建一个Receiver对象
     */
    @PostConstruct
    public void init() {
        for (int i = 0; i < GROUP_IDS.size(); i++) {
            context.getBean(Receiver.class);
        }
    }


    /**
     * 给每个Receiver对象的consumer方法，@KafkaListener赋值相应的groupId
     *
     * @return
     */
    @Bean
    public static KafkaListenerAnnotationBeanPostProcessor.AnnotationEnhancer groupIdEnhancer() {
        return (attrs, element) -> {
            if (element instanceof Method) {
                String name = ((Method) element).getDeclaringClass().getSimpleName() + "." + ((Method) element).getName();
                if (RECEIVER_METHOD_NAME.equals(name)) {
                    attrs.put("groupId", GROUP_IDS.get(index));
                    index++;
                }
            }
            return attrs;
        };
    }


    /**
     * 针对tag消息过滤
     *
     * @param tagIdKey   将tag写进header里
     * @param tagIdValue
     * @return
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory filterContainerFactory(@Value("${austin.business.tagId.key}") String tagIdKey,
                                                                          @Value("${austin.business.tagId.value}") String tagIdValue) {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setAckDiscarded(true);
        factory.setConsumerFactory(consumerFactory);

        factory.setRecordFilterStrategy(consumerRecord -> {
            if (Optional.ofNullable(consumerRecord.value()).isPresent()) {
                for (Header header : consumerRecord.headers()) {
                    if (header.key().equals(tagIdKey) && new String(header.value()).equals(new String(tagIdValue.getBytes(StandardCharsets.UTF_8)))) {
                        return false;
                    }
                }
            }
            //返回true，将会被丢弃
            return true;
        });
        return factory;
    }
}
