package org.enterprise.infrastructure.redis.adapter;

import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.infrastructure.ProductAbstractDelayQueue;
import org.enterprise.infrastructure.utils.spring.SpringContextUtil;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 * @author: albert.chen
 * @create: 2023-12-05
 * @description:
 */
@Component
public class RedisDelayAdapter extends ProductAbstractDelayQueue {


    @Override
    public void sendDelayMessage(DscheduleRequest dscheduleRequest) {
        RedissonClient redissonClient =  SpringContextUtil.getBean(RedissonClient.class);

    }
}
