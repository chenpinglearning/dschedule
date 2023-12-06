package org.enterprise.protocol.http;

import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.protocol.ConsumerHandler;
import org.enterprise.util.DscheduleThreadPool;
import org.enterprise.util.JacksonUtil;

/**
 * @author: albert.chen
 * @create: 2023-12-06
 * @description:
 */
public class DscheduleHttpConsumer implements ConsumerHandler {

    @Override
    public void callBackMessage(String message) {
        DscheduleRequest dscheduleRequest = JacksonUtil.string2Obj(message, DscheduleRequest.class);
        DscheduleThreadPool.consumerDscheduleThreadpool.execute(() -> {
            //you business deal
        });
    }
}
