package org.enterprise.constants;

/**
 * @author: albert.chen
 * @create: 2023-11-28
 * @description: Delayed scheduling type，default rabbitmq
 * 0----message delayed user rabbitmq(support message arbitrary delay , millisecond delay)
 * 1----message delayed user xxl_job and mysql (support message arbitrary delay , second delay)
 * 2----message delayed user redis(support message arbitrary delay , millisecond delay , window batch message)
 */

public enum DscheduleType {
    RABBITMQ(0, "rabbitMq"),
    XXL_JOB_MYSQL(1, "xxl_job_mysql"),
    REDIS(2, "redis");


    private int type;
    private String description;

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
