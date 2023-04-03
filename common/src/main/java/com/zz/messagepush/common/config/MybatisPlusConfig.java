package com.zz.messagepush.common.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */

@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig {

//    @Bean
//    public MybatisPlusInterceptor paginationInterceptor(){
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
//        return interceptor;
//    }
//
//    @Bean
//    @Primary
//    public MySqlInjector mySqlInjector(){
//        return new MySqlInjector();
//    }
}
