package com.zz.messagepush.support.utils;

import org.jetbrains.annotations.NotNull;
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

    private static final StandardEvaluationContext EVALUATION_CONTEXT;

    private static final PropertyPlaceholderHelper PROPERTY_PLACEHOLDER_HELPER = new PropertyPlaceholderHelper(PLACE_HOLDER_PREFIX, PLACE_HOLDER_SUFFIX);

    static {
        EVALUATION_CONTEXT = new StandardEvaluationContext();
        EVALUATION_CONTEXT.addPropertyAccessor(new MapAccessor());
    }

    public static String replaceHolder(final String template, final Map<String, String> paramMap) {
        return PROPERTY_PLACEHOLDER_HELPER.replacePlaceholders(template, new CustomPlaceholderResolver(template,paramMap));
    }

    private static class CustomPlaceholderResolver implements PropertyPlaceholderHelper.PlaceholderResolver {

        private final String template;
        private final Map<String, String> paramMap;

        public CustomPlaceholderResolver(String template,Map<String, String> paramMap) {
            super();
            this.template = template;
            this.paramMap = paramMap;
        }


        @Override
        public String resolvePlaceholder(@NotNull String placeholderName) {
            String value = paramMap.get(placeholderName);
            if (null == value) {
                String errorStr = MessageFormat.format("template:{0} require param:{1},but not exist! paramMap:{2}",
                        template, placeholderName, paramMap.toString());
                throw new IllegalArgumentException(errorStr);
            }
            return value;
        }
    }
}
