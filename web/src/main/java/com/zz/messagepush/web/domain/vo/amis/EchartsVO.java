package com.zz.messagepush.web.domain.vo.amis;

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
    private TitleVO titleVO;

    /**
     * 提示
     */
    private ToolTipVO toolTipVO;

    /**
     * 图例
     */
    private LegendVO legendVO;

    /**
     * x轴
     */
    private XAixsVO xAixsVO;

    /**
     * y轴
     */
    private YAixsVO yAxisVO;

    /**
     * 系统列表
     */
    private List<SeriesVO> seriesVOList;

    @Data
    @Builder
    public static class TitleVO {

        private String title;
    }

    @Data
    @Builder
    public static class ToolTipVO {

    }

    @Data
    @Builder
    public static class LegendVO {
        private List<String> data;
    }

    @Data
    @Builder
    public static class XAixsVO {
        private List<String> data;
    }

    @Data
    @Builder
    public static class YAixsVO {

    }

    @Data
    @Builder
    public static class SeriesVO {
        private String name;

        private String type;

        private List<Integer> data;
    }
}
