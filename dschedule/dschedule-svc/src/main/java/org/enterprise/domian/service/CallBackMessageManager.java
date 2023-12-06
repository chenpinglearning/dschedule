package org.enterprise.domian.service;

import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.domian.constants.CallWayEnum;
import org.enterprise.infrastructure.mysql.adapter.MysqlDelayAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: albert.chen
 * @create: 2023-12-06
 * @description:
 */
@Component
public class CallBackMessageManager {
    @Value("callBack.way")
    private String callBackWay;
    @Resource
    private MysqlDelayAdapter mysqlDelayAdapter;


    public void callBackDelayMessage(DscheduleRequest dscheduleRequest) {
        DelayMessageCallBackService delayMessageCallBackService = CallWayEnum.getDelayMessageCallBackService(callBackWay);
        Boolean delayResult = Boolean.TRUE;
        try {
            delayMessageCallBackService.callBack(dscheduleRequest);
        } catch (Exception e) {
            delayResult = Boolean.FALSE;
        }

        if (Boolean.FALSE.equals(delayResult)) {
            mysqlDelayAdapter.updateFailDelayMessage(dscheduleRequest.getSeqId());
        }
    }


}
