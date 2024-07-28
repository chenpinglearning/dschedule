package org.enterprise.infrastructure.rabbitmq.adapter;

import lombok.extern.slf4j.Slf4j;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.domian.service.CallBackMessageManager;
import org.enterprise.infrastructure.ProductAbstractDelayQueue;
import org.enterprise.infrastructure.rabbitmq.ExchangeEnum;
import org.enterprise.infrastructure.rabbitmq.QueueEnum;
import org.enterprise.infrastructure.utils.spring.SpringContextUtil;
import org.enterprise.util.JacksonUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
@Slf4j
@Component
public class RabbitMqDelayAdapter extends ProductAbstractDelayQueue {
    @Resource
    private CallBackMessageManager callBackMessageManager;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendDelayMessage(DscheduleRequest dscheduleRequest) {
        log.info("push delay message to rattimq {}", dscheduleRequest.getSeqId());
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDelay(dscheduleRequest.getDelayTime());
        Message message = new Message(JacksonUtil.obj2String(dscheduleRequest).getBytes(), messageProperties);

        rabbitTemplate.send(ExchangeEnum.DELAY_EXCHANGE.getValue(), QueueEnum.DELAY_MESSAGE_QUEUE.getRoutingKey(), message);
    }


    /**
     * consumer
     *
     * @param message
     */
    @RabbitListener(queues = {"dschedule.delay.message.queue"})
    public void delayQueue(Message message) {
        DscheduleRequest dscheduleRequest = JacksonUtil.string2Obj(new String(message.getBody()), DscheduleRequest.class);
        log.info("consumer delay message from rabbit {}", dscheduleRequest.getSeqId());

        callBackMessageManager.callBackDelayMessage(dscheduleRequest);

    }

}
