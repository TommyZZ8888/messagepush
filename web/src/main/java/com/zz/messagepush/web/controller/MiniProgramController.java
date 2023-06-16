package com.zz.messagepush.web.controller;

import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.hutool.http.HttpUtil;
import com.google.common.base.Throwables;

import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.support.utils.WxServiceUtil;
import com.zz.messagepush.web.domain.vo.amis.CommonAmisVo;
import com.zz.messagepush.web.utils.Convert4Amis;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.subscribemsg.TemplateInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;



/**
 * @Description MiniProgramController
 * @Author 张卫刚
 * @Date Created on 2023/6/16
 */
@Slf4j
@RestController
@RequestMapping("/miniProgram")
@Api("微信服务号")
@CrossOrigin(origins = {AustinConstant.ORIGIN_VALUE}, allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.PUT, RequestMethod.POST, RequestMethod.GET})
public class MiniProgramController {

    @Autowired
    private WxServiceUtil wxServiceUtils;

    @GetMapping("/template/list")
    @ApiOperation("/根据账号Id获取模板列表")
    public ResponseResult<Boolean> queryList(Long id) {
        try {
            List<CommonAmisVo> result = new ArrayList<>();
            WxMaSubscribeService wxMaSubscribeService = wxServiceUtils.getMiniProgramServiceMap().get(id);
            List<TemplateInfo> templateList = wxMaSubscribeService.getTemplateList();
            for (TemplateInfo templateInfo : templateList) {
                CommonAmisVo commonAmisVo = CommonAmisVo.builder().label(templateInfo.getTitle()).value(templateInfo.getPriTmplId()).build();
                result.add(commonAmisVo);
            }
            return ResponseResult.success("ok");
        } catch (Exception e) {
            log.error("MiniProgramController#queryList fail:{}", Throwables.getStackTraceAsString(e));
            return ResponseResult.fail(RespStatusEnum.SERVICE_ERROR.getDescription());
        }

    }

    /**
     * 根据账号Id和模板ID获取模板列表
     *
     * @return
     */
    @PostMapping("/detailTemplate")
    @ApiOperation("/根据账号Id和模板ID获取模板列表")
    public ResponseResult<Boolean> queryDetailList(Long id, String wxTemplateId) {
        if (id == null || wxTemplateId == null) {
            return ResponseResult.success(RespStatusEnum.CLIENT_BAD_PARAMETERS.getDescription());
        }
        try {
            WxMaSubscribeService wxMaSubscribeService = wxServiceUtils.getMiniProgramServiceMap().get(id);
            List<TemplateInfo> templateList = wxMaSubscribeService.getTemplateList();
            CommonAmisVo wxMpTemplateParam = Convert4Amis.getWxMaTemplateParam(wxTemplateId, templateList);
            return ResponseResult.success(wxMpTemplateParam.toString());
        } catch (Exception e) {
            log.error("MiniProgramController#queryDetailList fail:{}", Throwables.getStackTraceAsString(e));
            return ResponseResult.fail(RespStatusEnum.SERVICE_ERROR.getDescription());
        }
    }


    /**
     * 登录凭证校验
     *
     * @return
     */
    @GetMapping("/sync/openid")
    @ApiOperation("登录凭证校验")
    public ResponseResult<Boolean>  syncOpenId(String code) {
        String appId = "xxx";
        String secret = "xxxxx";
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String result = HttpUtil.get(url);
        System.out.println(result);
        return ResponseResult.success(result);
    }

}
