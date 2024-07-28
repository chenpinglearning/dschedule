package org.enterprise.domian.constants;

import org.enterprise.infrastructure.ProductAbstractDelayQueue;
import org.enterprise.infrastructure.mysql.adapter.MysqlDelayAdapter;
import org.enterprise.infrastructure.rabbitmq.adapter.RabbitMqDelayAdapter;
import org.enterprise.infrastructure.redis.adapter.RedisDelayAdapter;

/**
 * @author: albert.chen
 * @create: 2023-12-05
 * @description:
 */
public enum DelayDealWayEnum {
    RABBITMQ(0, "rabbitMq", RabbitMqDelayAdapter.class),
    REDIS(1, "redis",  RedisDelayAdapter.class);

    private final Integer way;
    private final String msg;
    private final Class productAbstractDelayQueue;

    DelayDealWayEnum(Integer way, String msg, Class productAbstractDelayQueue) {
        this.way = way;
        this.msg = msg;
        this.productAbstractDelayQueue = productAbstractDelayQueue;
    }

    public static Class getAbstractDelayQueue(Integer way) {
        for (DelayDealWayEnum value : DelayDealWayEnum.values()) {
            if (value.getWay().equals(way)) {
                return value.productAbstractDelayQueue;
            }
        }
        return RABBITMQ.productAbstractDelayQueue;
    }

    public Integer getWay() {
        return way;
    }

    public String getMsg() {
        return msg;
    }
}
