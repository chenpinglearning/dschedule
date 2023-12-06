package org.enterprise.infrastructure.redis.adapter;

import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.infrastructure.ProductAbstractDelayQueue;

/**
 * @author: albert.chen
 * @create: 2023-12-05
 * @description:
 */
public class RedisDelayAdapter extends ProductAbstractDelayQueue {


    @Override
    public void sendDelayMessage(DscheduleRequest dscheduleRequest) {

    }
}
