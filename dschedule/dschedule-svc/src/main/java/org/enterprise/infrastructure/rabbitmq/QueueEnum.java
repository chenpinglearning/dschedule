package org.enterprise.infrastructure.rabbitmq;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
public enum QueueEnum {
    /**
     * delay
     */
    DELAY_MESSAGE_QUEUE("dschedule.delay.message.queue", "dschedule.delay.message.routingKey");
    /**
     * name
     */
    private final String name;
    /**
     * routingKey
     */
    private final String routingKey;

    QueueEnum(String name, String routingKey) {
        this.name = name;
        this.routingKey = routingKey;
    }

    public String getName() {
        return name;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}
