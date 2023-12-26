package org.enterprise.infrastructure.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
@Conditional(value = RabbitMqCondition.class)
@Configuration
public class RabbitMqConfig {

    @Bean
    public Exchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(ExchangeEnum.DELAY_EXCHANGE.getValue(), "x-delayed-message", true, false, args);
    }


    @Bean
    public Queue delayQueue() {
        return new Queue(QueueEnum.DELAY_MESSAGE_QUEUE.getName(), true);
    }

    @Bean
    public Binding deplyBinding(Queue queue) {
        return BindingBuilder.bind(queue).to(delayExchange()).with(QueueEnum.DELAY_MESSAGE_QUEUE.getRoutingKey()).noargs();
    }
}
