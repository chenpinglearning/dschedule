package org.enterprise.domian.service;

import lombok.extern.slf4j.Slf4j;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.constants.Constant;
import org.enterprise.domian.constants.CallWayEnum;
import org.enterprise.infrastructure.mysql.adapter.MysqlDelayAdapter;
import org.enterprise.util.JacksonUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: albert.chen
 * @create: 2023-12-06
 * @description:
 */
@Slf4j
@Component
public class CallBackMessageManager {
    @Resource
    private MysqlDelayAdapter mysqlDelayAdapter;


    public void callBackDelayMessage(DscheduleRequest dscheduleRequest) {
        String callBackWay = null;
        String callBackUrl = (String) dscheduleRequest.getExtraParam().get(Constant.CallBackUrl);
        if (callBackUrl.startsWith(Constant.http) || callBackUrl.startsWith(Constant.https)) {
            callBackWay = Constant.http;
        } else {
            callBackWay = Constant.kafka;
        }

        DelayMessageCallBackService delayMessageCallBackService = CallWayEnum.getDelayMessageCallBackService(callBackWay);
        try {
            delayMessageCallBackService.callBack(dscheduleRequest);
        } catch (Exception e) {
            log.error("callBack message deal error {}", JacksonUtil.obj2String(dscheduleRequest));
        }
    }


}
