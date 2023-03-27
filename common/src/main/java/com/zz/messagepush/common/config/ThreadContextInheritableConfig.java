package com.zz.messagepush.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.annotation.PostConstruct;


@Component
public class ThreadContextInheritableConfig {

    @Autowired
    RequestContextFilter requestContextFilter;


    @Autowired
    DispatcherServlet dispatcherServlet;

    @PostConstruct
    public void init() {
        requestContextFilter.setThreadContextInheritable(true);
        dispatcherServlet.setThreadContextInheritable(true);
    }
}
