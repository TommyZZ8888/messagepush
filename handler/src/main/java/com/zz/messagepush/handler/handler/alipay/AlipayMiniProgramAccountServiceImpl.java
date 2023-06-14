package com.zz.messagepush.handler.handler.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayOpenAppMiniTemplatemessageSendModel;
import com.alipay.api.request.AlipayOpenAppMiniTemplatemessageSendRequest;
import com.zz.messagepush.common.domain.dto.account.AlipayMiniProgramAccount;
import com.zz.messagepush.handler.config.AlipayClientSingleton;
import com.zz.messagepush.handler.domain.alipay.AlipayMiniProgramParam;
import com.zz.messagepush.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Description AlipayMiniProgramAccountServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/6/12
 */

@Service
@Slf4j
public class AlipayMiniProgramAccountServiceImpl implements AlipayMiniProgramAccountService {

    @Autowired
    private AccountUtils accountUtils;

    @Override
    public void send(AlipayMiniProgramParam alipayMiniProgramParam) throws AlipayApiException {
        AlipayMiniProgramAccount account = accountUtils.getAccountById(alipayMiniProgramParam.getSendAccount(), AlipayMiniProgramAccount.class);
        AlipayClient alipayClient = AlipayClientSingleton.getSingleton(account);
        List<AlipayOpenAppMiniTemplatemessageSendRequest> requests = assemblyReq(alipayMiniProgramParam, account);
        for (AlipayOpenAppMiniTemplatemessageSendRequest request : requests) {
            alipayClient.execute(request);
        }
    }


    private List<AlipayOpenAppMiniTemplatemessageSendRequest> assemblyReq(AlipayMiniProgramParam alipayMiniProgramParam, AlipayMiniProgramAccount account) {
        Set<String> receivers = alipayMiniProgramParam.getToUserId();
        List<AlipayOpenAppMiniTemplatemessageSendRequest> list = new ArrayList<>(receivers.size());

        for (String receiver : receivers) {
            AlipayOpenAppMiniTemplatemessageSendRequest request = new AlipayOpenAppMiniTemplatemessageSendRequest();
            AlipayOpenAppMiniTemplatemessageSendModel model = new AlipayOpenAppMiniTemplatemessageSendModel();
            model.setData(alipayMiniProgramParam.getData().toString());
            model.setToUserId(receiver);
            model.setPage(account.getPage());
            model.setUserTemplateId(account.getUserTemplateId());
            request.setBizModel(model);
            list.add(request);
        }
        return list;
    }


}
