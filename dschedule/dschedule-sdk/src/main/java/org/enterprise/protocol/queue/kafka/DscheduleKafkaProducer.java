package org.enterprise.protocol.queue.kafka;

import org.enterprise.protocol.ProducerHandler;

/**
 * @author: albert.chen
 * @create: 2023-11-29
 * @description:
 */
public class DscheduleKafkaProducer implements ProducerHandler {


    @Override
    public void sendDelayMessage(String delayMessage) throws Exception {
        //  KafkaProducerManager.sendMessage(delayMessage);
    }
}
