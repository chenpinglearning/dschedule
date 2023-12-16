package org.enterprise.protocol.http;

import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.constants.EnvironmentConfig;
import org.enterprise.protocol.ConsumerHandler;
import org.enterprise.util.JacksonUtil;

import java.util.Objects;

/**
 * @author: albert.chen
 * @create: 2023-12-06
 * @description:
 */
public class DscheduleHttpConsumer implements ConsumerHandler {

    @Override
    public void callBackMessage(String message) {
        DscheduleRequest dscheduleRequest = JacksonUtil.string2Obj(message, DscheduleRequest.class);
        String appId = System.getProperty(EnvironmentConfig.APP_ID);
        if (Objects.equals(appId, dscheduleRequest.getAppId())) {
            return;
        }

        //you business
        delayCallBackMessage(dscheduleRequest);
    }


    protected void delayCallBackMessage(DscheduleRequest dscheduleRequest) {

    }
}
