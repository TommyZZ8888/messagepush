package com.zz.messagepush.web.utils;

/**
 * @Description Convert4Amis
 * @Author 张卫刚
 * @Date Created on 2023/6/16
 */

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zz.messagepush.common.constant.SendAccountConstant;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.support.domain.entity.ChannelAccountEntity;
import com.zz.messagepush.web.domain.vo.amis.CommonAmisVo;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.subscribemsg.TemplateInfo;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
public class Convert4Amis {

    /**
     * 标识忽略
     */
    public static final int IGNORE_TG = 0;
    /**
     * 标识已读取到'$'字符
     */
    public static final int START_TG = 1;
    /**
     * 标识已读取到'{'字符
     */
    public static final int READ_TG = 2;


    /**
     * 需要打散的字段(将json字符串打散为一个一个字段返回）
     * (主要是用于回显数据)
     */
    private static final List<String> FLAT_FIELD_NAME = Arrays.asList("msgContent");

    /**
     * 需要格式化为jsonArray返回的字段
     * (前端是一个JSONArray传递进来)
     */
    private static final List<String> PARSE_JSON_ARRAY = Arrays.asList("feedCards", "btns");

    /**
     * 需要打散的字段(将json字符串打散为一个一个字段返回）
     * (主要是用于回显数据)
     * @@ -45,7 +53,7 @@ public class Convert4Amis {
     * /**
     * (前端是一个JSONObject传递进来，返回一个JSONArray回去)
     */
    private static final List<String> PARSE_JSON_OBJ_TO_ARRAY = Arrays.asList("officialAccountParam", "miniProgramParam");

    /**
     * 钉钉工作消息OA实际的映射
     */
    private static final List<String> DING_DING_OA_FIELD = Arrays.asList("dingDingOaHead", "dingDingOaBody");
    /**
     * 钉钉OA字段名实际的映射
     */
    private static final Map<String, String> DING_DING_OA_NAME_MAPPING = new HashMap<>();

    static {
        DING_DING_OA_NAME_MAPPING.put("bgcolor", "dingDingOaHeadBgColor");
        DING_DING_OA_NAME_MAPPING.put("text", "dingDingOaHeadTitle");
        DING_DING_OA_NAME_MAPPING.put("title", "dingDingOaTitle");
        DING_DING_OA_NAME_MAPPING.put("image", "media_id");
        DING_DING_OA_NAME_MAPPING.put("author", "dingDingOaAuthor");
        DING_DING_OA_NAME_MAPPING.put("content", "dingDingOaContent");
    }

    /**
     * 将List对象转换成Map(无嵌套)
     * @param param
     * @return
     */
    public static <T> List<Map<String, Object>> flatListMap(List<T> param) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (T t : param) {
            Map<String, Object> map = flatSingleMap(t);
            result.add(map);
        }
        return result;
    }

    /**
     * 将单个对象转换成Map(无嵌套)
     * <p>
     * 主要兼容amis的回显(前端不用amis可忽略)
     * @param obj
     * @return
     */
    public static Map<String, Object> flatSingleMap(Object obj) {
        Map<String, Object> result = MapUtil.newHashMap(32);
        Field[] fields = ReflectUtil.getFields(obj.getClass());
        for (Field field : fields) {
            if (FLAT_FIELD_NAME.contains(field.getName())) {
                String fieldValue = (String) ReflectUtil.getFieldValue(obj, field);
                JSONObject jsonObject = JSONObject.parseObject(fieldValue);
                for (String key : jsonObject.keySet()) {
                    /**
                     * 钉钉OA消息回显
                     */
                    if (DING_DING_OA_FIELD.contains(key)) {
                        JSONObject object = jsonObject.getJSONObject(key);
                        for (String objKey : object.keySet()) {
                            result.put(DING_DING_OA_NAME_MAPPING.get(objKey), object.getString(objKey));
                        }
                    } else if (PARSE_JSON_ARRAY.contains(key)) {
                        /**
                         * 部分字段是直接传入数组，把数组直接返回(用于回显)
                         */
                        result.put(key, JSON.parseArray(jsonObject.getString(key)));
                    } else if (PARSE_JSON_OBJ_TO_ARRAY.contains(key)) {
                        /**
                         * 部分字段是直接传入Obj，把数组直接返回(用于回显)
                         */
                        String value = "[" + jsonObject.getString(key) + "]";
                        result.put(key, JSON.parseArray(value));
                    } else {
                        result.put(key, jsonObject.getString(key));
                    }
                }
            }
            result.put(field.getName(), ReflectUtil.getFieldValue(obj, field));
        }
        return result;
    }

    /**
     * 【这个方法不用看】，纯粹为了适配amis前端
     * <p>
     * 得到模板的参数 组装好 返回给前端展示
     * @param wxTemplateId
     * @param allPrivateTemplate
     * @return
     */
    public static CommonAmisVo getWxMpTemplateParam(String wxTemplateId, List<WxMpTemplate> allPrivateTemplate) {
        CommonAmisVo officialAccountParam = null;
        for (WxMpTemplate wxMpTemplate : allPrivateTemplate) {
            if (wxTemplateId.equals(wxMpTemplate.getTemplateId())) {
                String[] data = wxMpTemplate.getContent().split(StrUtil.LF);
                officialAccountParam = CommonAmisVo.builder()
                        .type("input-table")
                        .name("officialAccountParam")
                        .addable(true)
                        .editable(true)
                        .needConfirm(false)
                        .build();
                List<CommonAmisVo.ColumnsDTO> columnsDTOS = new ArrayList<>();
                for (String datum : data) {
                    String name = datum.substring(datum.indexOf("{{") + 2, datum.indexOf("."));
                    CommonAmisVo.ColumnsDTO.ColumnsDTOBuilder dtoBuilder = CommonAmisVo.ColumnsDTO.builder().name(name).type("input-text").required(true).quickEdit(true);
                    if (datum.contains("first")) {
                        dtoBuilder.label("名字");
                    } else if (datum.contains("remark")) {
                        dtoBuilder.label("备注");
                    } else {
                        dtoBuilder.label(datum.split("：")[0]);
                    }
                    columnsDTOS.add(dtoBuilder.build());
                }
                officialAccountParam.setColumns(columnsDTOS);

            }
        }
        return officialAccountParam;
    }

    public static CommonAmisVo getWxMpQrCode(String url, String id) {
        CommonAmisVo image = CommonAmisVo.builder().type("static-image").value(url).originalSrc(url).name("image").label("扫描关注").fixedSize(true).fixedSizeClassName(url).fixedSizeClassName("h-32").build();
        String requestAdaptor = "var openId = localStorage.getItem(\"openId\");\n" +
                "if (openId != null && openId != 'null' && openId != '' && openId !== undefined) {\n" +
                "    alert(\"已登录，你的ID是：\" + openId);\n" +
                "    window.location.href = 'index.html';\n" +
                "    return api;\n" +
                "}";


        String adaptor = "if (payload.data != 'NO_LOGIN' && payload.status == '0') {\n" +
                "    localStorage.setItem(\"openId\", payload.data.openId);\n" +
                "    alert(\"扫码已登录成功，你的ID是：\" + payload.data.openId);\n" +
                "    window.location.href = 'index.html';\n" +
                "}\n" +
                "return payload;";


        CommonAmisVo service = CommonAmisVo.builder().type("service").api(CommonAmisVo.ApiDTO.builder().url("${ls:backend_url}/officialAccount/check/login?sceneId=" + id)
                .adaptor(adaptor).requestAdaptor(requestAdaptor).build()).interval(2000).silentPolling(true).build();

        return CommonAmisVo.builder().type("form").title("登录").mode("horizontal").body(Arrays.asList(image, service)).build();
    }

    /**
     * 【这个方法不用看】，纯粹为了适配amis前端
     * <p>
     * 得到微信服务号的【带参数】二维码返回给前端
     * @return
     */
    public static String getLoginJsonp(String userInfo) {
        if (StrUtil.isBlank(userInfo)) {
            log.error("can't get userInfo!");
            return "(function() {})();";
        } else {
            return "(function() {\n" +
                    "\tlocalStorage.setItem(\"openId\", \"123\");\n" +
                    "\tlocalStorage.setItem(\"userName\", \"333\");\n" +
                    "\twindow.location.href='index.html';\n" +
                    "})();";
        }
    }

    /**
     * 【这个方法不用看】，纯粹为了适配amis前端
     * <p>
     * 获取占位符的参数
     * @param msgContent
     * @return
     */
    public static CommonAmisVo getTestContent(String msgContent) {
        Set<String> placeholderList = getPlaceholderList(msgContent);
        if (CollUtil.isEmpty(placeholderList)) {
            return null;
        }

        // placeholderList!=null  说明有占位符
        CommonAmisVo testParam = CommonAmisVo.builder()
                .type("input-table")
                .name("testParam")
                .addable(true)
                .editable(true)
                .needConfirm(false)
                .build();
        List<CommonAmisVo.ColumnsDTO> columnsDTOS = new ArrayList<>();
        for (String param : placeholderList) {
            CommonAmisVo.ColumnsDTO dto = CommonAmisVo.ColumnsDTO.builder().name(param).label(param).type("input-text").required(true).quickEdit(true).build();
            columnsDTOS.add(dto);
        }
        testParam.setColumns(columnsDTOS);
        return testParam;
    }

    /**
     * 获取占位符的参数
     * @param content
     * @return
     */
    public static Set<String> getPlaceholderList(String content) {
        char[] textChars = content.toCharArray();
        StringBuilder textSofar = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        // 存储占位符 位置信息集合
        List<String> placeholderList = new ArrayList<>();
        // 当前标识
        int modeTg = IGNORE_TG;
        for (int m = 0; m < textChars.length; m++) {
            char c = textChars[m];
            textSofar.append(c);
            switch (c) {
                case '{': {
                    modeTg = START_TG;
                    sb.append(c);
                }
                break;
                case '$': {
                    if (modeTg == START_TG) {
                        sb.append(c);
                        modeTg = READ_TG;
                    } else {
                        if (modeTg == READ_TG) {
                            sb = new StringBuilder();
                            modeTg = IGNORE_TG;
                        }
                    }
                }
                break;
                case '}': {
                    if (modeTg == READ_TG) {
                        modeTg = IGNORE_TG;
                        sb.append(c);
                        String str = sb.toString();
                        if (StrUtil.isNotEmpty(str)) {
                            placeholderList.add(str);
                            textSofar = new StringBuilder();
                        }
                        sb = new StringBuilder();
                    } else if (modeTg == START_TG) {
                        modeTg = IGNORE_TG;
                        sb = new StringBuilder();
                    }
                    break;
                }
                default: {
                    if (modeTg == READ_TG) {
                        sb.append(c);
                    } else if (modeTg == START_TG) {
                        modeTg = IGNORE_TG;
                        sb = new StringBuilder();
                    }
                }
            }
        }
        Set<String> result = placeholderList.stream().map(s -> s.replaceAll("\\{", "").replaceAll("\\$", "").replaceAll("\\}", "")).collect(Collectors.toSet());
        return result;
    }

    /**
     * 【这个方法不用看】，纯粹为了适配amis前端
     * <p>
     * 得到模板的参数 组装好 返回给前端展示
     * @param wxTemplateId
     * @param templateList
     * @return
     */
    public static CommonAmisVo getWxMaTemplateParam(String wxTemplateId, List<TemplateInfo> templateList) {
        CommonAmisVo officialAccountParam = null;
        for (TemplateInfo templateInfo : templateList) {
            if (wxTemplateId.equals(templateInfo.getPriTmplId())) {
                String[] data = templateInfo.getContent().split(StrUtil.LF);
                officialAccountParam = CommonAmisVo.builder()
                        .type("input-table")
                        .name("miniProgramParam")
                        .addable(true)
                        .editable(true)
                        .needConfirm(false)
                        .build();
                List<CommonAmisVo.ColumnsDTO> columnsDTOS = new ArrayList<>();
                for (String datum : data) {
                    String name = datum.substring(datum.indexOf("{{") + 2, datum.indexOf("."));
                    String label = datum.split(":")[0];
                    CommonAmisVo.ColumnsDTO columnsDTO = CommonAmisVo.ColumnsDTO.builder()
                            .name(name).type("input-text").required(true).quickEdit(true).label(label).build();
                    columnsDTOS.add(columnsDTO);
                }
                officialAccountParam.setColumns(columnsDTOS);

            }
        }
        return officialAccountParam;

    }

    /**
     * 【这个方法不用看】，纯粹为了适配amis前端
     * <p>
     * 得到渠道账号信息，返回给前端做展示
     * @return
     */
    public static List<CommonAmisVo> getChannelAccountVo(List<ChannelAccountEntity> channelAccounts,Integer channelType) {
        List<CommonAmisVo> result = new ArrayList<>();
        if (ChannelType.SMS.getCode().equals(channelType)) {
            CommonAmisVo commonAmisVo = CommonAmisVo.builder().label("AUTO").value("0").build();
            result.add(commonAmisVo);
        }
        for (ChannelAccountEntity channelAccount : channelAccounts) {
            CommonAmisVo commonAmisVo = CommonAmisVo.builder().label(channelAccount.getName()).value(String.valueOf(channelAccount.getId())).build();
            result.add(commonAmisVo);
        }
        return result;
    }

}
