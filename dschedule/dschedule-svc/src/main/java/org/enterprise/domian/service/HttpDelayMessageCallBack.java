package org.enterprise.domian.service;


import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.protocol.http.HttpClientManage;
import org.enterprise.util.JacksonUtil;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
public class HttpDelayMessageCallBack extends DelayMessageCallBackService {

    @Override
    protected void callBackDelayMessage(DscheduleRequest dscheduleRequest) throws Exception {
        String callBackUrl = (String) dscheduleRequest.getExtraParam().get("call_back_url");
        HttpClientManage.postJson(callBackUrl, JacksonUtil.obj2String(dscheduleRequest));
    }

}
