package org.enterprise.infrastructure.redis.adapter;

import lombok.extern.slf4j.Slf4j;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.infrastructure.ProductAbstractDelayQueue;
import org.enterprise.infrastructure.redis.timeWheel.service.TimeWheelService;
import org.enterprise.util.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: albert.chen
 * @create: 2023-12-05
 * @description:
 */
@Slf4j
@Component
public class RedisDelayAdapter extends ProductAbstractDelayQueue {
    @Resource
    private TimeWheelService timeWheelService;

    @Override
    public void sendDelayMessage(DscheduleRequest dscheduleRequest) throws Exception {
        log.info("sendDelayMessage to redis {}", JacksonUtil.obj2String(dscheduleRequest));
        timeWheelService.addTask(dscheduleRequest);
    }
}
