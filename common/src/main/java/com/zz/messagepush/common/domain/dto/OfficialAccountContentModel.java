package com.zz.messagepush.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfficialAccountContentModel extends ContentModel {

    /**
     * 模板消息发送的数据
     */
    public Map<String,String> map;

    /**
     * 模板消息跳转的url
     */
    public String url;

}
