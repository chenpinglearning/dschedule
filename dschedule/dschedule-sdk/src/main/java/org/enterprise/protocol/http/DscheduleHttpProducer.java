package org.enterprise.protocol.http;

import org.enterprise.protocol.ProducerHandler;

/**
 * @author: albert.chen
 * @create: 2023-11-28
 * @description:
 */
public class DscheduleHttpProducer implements ProducerHandler {

    @Override
    public void sendDelayMessage(String delayMessage) throws Exception {
        HttpClientManage.postJson(delayMessage);
    }


}
