package org.enterprise.constants;

/**
 * @author: albert.chen
 * @create: 2023-11-28
 * @description: Delayed scheduling type，default rabbitmq
 * 0----message delayed user rabbitmq(support message arbitrary delay , millisecond delay)
 * 1----message delayed user redis(support message arbitrary delay , millisecond delay , window batch message)
 */

public enum DscheduleType {
    RABBITMQ(0, "rabbitMq"),
    REDIS(1, "redis");


    private final int type;
    private final String description;

    DscheduleType(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
