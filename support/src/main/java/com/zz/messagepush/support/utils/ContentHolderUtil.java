package com.zz.messagepush.support.utils;

import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.PropertyPlaceholderHelper;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @Description 内容占位符 替换
 * austin占位符格式 {$var}
 * @Author 张卫刚
 * @Date Created on 2023/3/17
 */
public class ContentHolderUtil {


    private static final String PLACE_HOLDER_PREFIX = "{$";

    private static final String PLACE_HOLDER_SUFFIX = "}";

    private static final StandardEvaluationContext evaluationContext;

    private static PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper(PLACE_HOLDER_PREFIX, PLACE_HOLDER_SUFFIX);

    static {
        evaluationContext = new StandardEvaluationContext();
        evaluationContext.addPropertyAccessor(new MapAccessor());
    }

    public static String replaceHolder(final String template, final Map<String, String> paramMap) {
        String replacePlaceholders = propertyPlaceholderHelper.replacePlaceholders(template, new CustomPlaceholderResolver(paramMap));
        return replacePlaceholders;
    }

    private static class CustomPlaceholderResolver implements PropertyPlaceholderHelper.PlaceholderResolver {
        private Map<String, String> paramMap;

        public CustomPlaceholderResolver(Map<String, String> paramMap) {
            super();
            this.paramMap = paramMap;
        }


        @Override
        public String resolvePlaceholder(String placeholderName) {
            String value = paramMap.get(placeholderName);
            if (null == value) {
                String errorStr = MessageFormat.format("template:{} require param:{},but not exist! paramMap:{}",
                        placeholderName, paramMap.toString());
                throw new IllegalArgumentException(errorStr);
            }
            return value;
        }
    }
}
