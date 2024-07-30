package org.enterprise.infrastructure.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.infrastructure.ProductAbstractDelayQueue;
import org.enterprise.util.JacksonUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: albert.chen
 * @create: 2023-12-05
 * @description:
 */
@Slf4j
@Component
public class KafkaDelayAdapter {
    @Resource
    private KafkaProducer <String , String> kafkaProducer;

    public void callBackDelayMessage(DscheduleRequest dscheduleRequest) {
        log.info("callBackDelayMessage by kafka {}" , JacksonUtil.obj2String(dscheduleRequest));
        ProducerRecord producerRecord = new ProducerRecord(KafkaConstants.dscheduleCallBackTopic ,JacksonUtil.obj2String(dscheduleRequest));
        kafkaProducer.send(producerRecord);
    }
}
