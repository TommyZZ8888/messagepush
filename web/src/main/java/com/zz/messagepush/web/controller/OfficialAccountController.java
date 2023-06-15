package com.zz.messagepush.web.controller;

import cn.hutool.core.util.StrUtil;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.constant.CommonConstant;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.support.utils.WxServiceUtil;
import com.zz.messagepush.web.domain.vo.amis.CommonAmisVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description OfficialAccountController
 * @Author 张卫刚
 * @Date Created on 2023/6/14
 */

@Api("微信服务号")
@RestController
@RequestMapping("/officialAccount")
public class OfficialAccountController {

    @Autowired
    private WxServiceUtil wxServiceUtil;

    @RequestMapping(value = "/templateList", method = RequestMethod.GET)
    @ApiOperation("根据模板id获取模板列表")
    public ResponseResult<List<CommonAmisVo>> queryList(Long id) throws WxErrorException {
        List<CommonAmisVo> list = new ArrayList<>();
        WxMpService wxMpService = wxServiceUtil.getOfficialAccountServiceMap().get(id);
        List<WxMpTemplate> allPrivateTemplate = wxMpService.getTemplateMsgService().getAllPrivateTemplate();
        for (WxMpTemplate wxMpTemplate : allPrivateTemplate) {
            CommonAmisVo commonAmisVo = CommonAmisVo.builder().label(wxMpTemplate.getTitle()).value(wxMpTemplate.getTemplateId()).build();
            list.add(commonAmisVo);
        }
        return ResponseResult.success("ok", list);
    }


    @RequestMapping(value = "/detailTemplate", method = RequestMethod.GET)
    @ApiOperation("根据模板id和账号id获取模板列表")
    public ResponseResult<List<CommonAmisVo>> queryDetailList(Long id, String wxTemplateId) throws WxErrorException {
        List<CommonAmisVo> list = new ArrayList<>();
        WxMpService wxMpService = wxServiceUtil.getOfficialAccountServiceMap().get(id);
        List<WxMpTemplate> allPrivateTemplate = wxMpService.getTemplateMsgService().getAllPrivateTemplate();
        for (WxMpTemplate wxMpTemplate : allPrivateTemplate) {
            if (wxTemplateId.equals(wxMpTemplate.getTemplateId())){
                String[] data = wxMpTemplate.getContent().split(StrUtil.LF);
                for (String datum : data) {
                    String name = datum.substring(datum.indexOf("{{") + 2, datum.indexOf("."));
                    CommonAmisVo commonAmisVo = CommonAmisVo.builder()
                            .name(name)
                            .type("text")
                            .required(true)
                            .build();
                    if (datum.contains("first")) {
                        commonAmisVo.setLabel("名字");
                    } else if (datum.contains("remark")) {
                        commonAmisVo.setLabel("备注");
                    } else {
                        commonAmisVo.setLabel(datum.split("：")[0]);

                    }
                    list.add(commonAmisVo);
                }
            }
        }
        return ResponseResult.success("ok", list);
    }
}
