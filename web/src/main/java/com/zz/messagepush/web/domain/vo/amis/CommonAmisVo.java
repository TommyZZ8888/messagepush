package com.zz.messagepush.web.domain.vo.amis;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description amis的通用转化类
 * @Author 张卫刚
 * @Date Created on 2023/6/14
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonAmisVo {

    private String type;

    private String label;

    private String value;

    private String name;

    private boolean required;

    private String size;

    private boolean addable;

    private boolean editable;

    private boolean needConfirm;

    private String width;

    private boolean fixedSize;
    private String fixedSizeClassName;
    private String frameImage;
    private String originalSrc;
    private Integer interval;
    private String target;
    private String mode;
    private String schemaApi;
    private String id;

    private String varParam;
    private boolean silentPolling;

    private List<CommonAmisVo> body;

    private String height;

    private String src;


    private String title;

    private String imageMode;

    /**
     * columns
     */
    @JSONField(name = "columns")
    private List<ColumnsDTO> columns;

    private ApiDTO api;

    /**
     * ColumnsDTO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class ColumnsDTO {
        /**
         * nameX
         */
        @JSONField(name = "name")
        private String name;
        /**
         * labelX
         */
        @JSONField(name = "label")
        private String label;

        /**
         * type
         */
        @JSONField(name = "type")
        private String type;
        /**
         * placeholder
         */
        @JSONField(name = "placeholder")
        private String placeholder;

        /**
         * type
         */
        @JSONField(name = "required")
        private Boolean required;

        @JSONField(name = "quickEdit")
        private Boolean quickEdit;

    }

    /**
     * ApiDTO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class ApiDTO {
        /**
         * adaptor
         */
        @JSONField(name = "adaptor")
        private String adaptor;

        /**
         * adaptor
         */
        @JSONField(name = "requestAdaptor")
        private String requestAdaptor;

        /**
         * url
         */
        @JSONField(name = "url")
        private String url;

    }
}
