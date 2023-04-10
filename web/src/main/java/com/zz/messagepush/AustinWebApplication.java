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
        // TODO apollo的ip/port【must】
        System.setProperty("apollo.config-service", "http://ip:7000");
        SpringApplication.run(AustinWebApplication.class, args);
    }

    private final Logger logger = LoggerFactory.getLogger(AustinWebApplication.class);


}
