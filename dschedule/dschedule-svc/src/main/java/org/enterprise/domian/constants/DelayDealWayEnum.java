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
    RABBITMQ(0, "rabbitMq", new RabbitMqDelayAdapter()),
    XXL_JOB_MYSQL(1, "xxl_job_mysql", new MysqlDelayAdapter()),
    REDIS(2, "redis", new RedisDelayAdapter());

    private Integer way;
    private String msg;
    private ProductAbstractDelayQueue productAbstractDelayQueue;
    DelayDealWayEnum(Integer way, String msg, ProductAbstractDelayQueue productAbstractDelayQueue) {
        this.way = way;
        this.msg = msg;
        this.productAbstractDelayQueue = productAbstractDelayQueue;
    }

    public static ProductAbstractDelayQueue getAbstractDelayQueue(Integer way) {
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
