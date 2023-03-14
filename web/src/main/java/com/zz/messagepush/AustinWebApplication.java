package com.zz.messagepush;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@SpringBootApplication
@EnableApolloConfig(value = {"dynamic-tp-apollo-dtp.yml"})
public class AustinWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(AustinWebApplication.class, args);
    }

    private final Logger logger = LoggerFactory.getLogger(AustinWebApplication.class);


}
