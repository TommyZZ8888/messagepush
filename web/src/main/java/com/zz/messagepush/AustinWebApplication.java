package com.zz.messagepush;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableApolloConfig(value = {"dynamic-tp-apollo-dtp.yml"})
public class AustinWebApplication {
    public static void main(String[] args) {
        /** TODO [optional]
         * 如果你需要动态配置
         * 1.启动apollo
         * 2.将下方application.yml中 Apollo.enabled改为true
         * 3.下方ip:port输入真实的ip port
         */
        System.setProperty("apollo.config-service", "http://ip:port");
        SpringApplication.run(AustinWebApplication.class, args);
    }

    private final Logger logger = LoggerFactory.getLogger(AustinWebApplication.class);


}
