package com.zz.messagepush.web.domain.vo.amis;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/11
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EchartsVO {

    /**
     * 标题
     */
    @JsonProperty
    private TitleVO title;

    /**
     * 提示
     */
    @JsonProperty
    private ToolTipVO tooltip;

    /**
     * 图例
     */
    @JsonProperty
    private LegendVO legend;

    /**
     * x轴
     */
    @JsonProperty
    private XAxisVO xAxis;

    /**
     * y轴
     */
    @JsonProperty
    private YAxisVO yAxis;

    /**
     * 系统列表
     */
    @JsonProperty
    private List<SeriesVO> series;

    @Data
    @Builder
    public static class TitleVO {

        private String text;
    }

    @Data
    @Builder
    public static class ToolTipVO {
        private String color;
    }

    @Data
    @Builder
    public static class LegendVO {
        private List<String> data;
    }

    @Data
    @Builder
    public static class XAxisVO {
        private List<String> data;
    }

    @Data
    @Builder
    public static class YAxisVO {
        private String type;
    }

    @Data
    @Builder
    public static class SeriesVO {
        private String name;

        private String type;

        private List<Integer> data;
    }
}
