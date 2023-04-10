package com.zz.messagepush.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:ApplicationContextUtil
 * @Description:
 * @Author: vren
 * @Date: 2022/5/23 15:45
 */
@Component
@Slf4j
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    private static final List<String> XML_PATH = new ArrayList<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ApplicationContextUtil.applicationContext == null) {
            ApplicationContextUtil.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }


    public static synchronized ApplicationContext loadContext(String path) {
        return loadContext(new String[]{path});
    }

    public static synchronized ApplicationContext loadContext(String[] paths) {
        if (null != paths && paths.length > 0) {
            List<String> newPaths = new ArrayList<>();
            for (String path : paths) {
                if (XML_PATH.contains(path)) {
                    log.info("ApplicationContext add new path:{}", path);
                    newPaths.add(path);
                } else {
                    log.info("ApplicationContext already add new path:{}", path);
                }
            }
            if (!newPaths.isEmpty()) {
                String[] array = new String[newPaths.size()];
                for (int i = 0; i < newPaths.size(); i++) {
                    array[i] = newPaths.get(i);
                    XML_PATH.add(array[i]);
                }
                if (null == applicationContext) {
                    applicationContext = new ClassPathXmlApplicationContext(array);
                } else {
                    applicationContext = new ClassPathXmlApplicationContext(array, applicationContext);
                }
            }
        }
        return applicationContext;
    }

}
