package org.enterprise.infrastructure.redis.timeWheel.dao;

import lombok.extern.slf4j.Slf4j;
import org.enterprise.infrastructure.redis.timeWheel.constants.TimeWheelConstant;
import org.enterprise.infrastructure.redis.timeWheel.entity.TaskEntry;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TimeWheelDao {
    @Resource
    private RedissonClient redissonClient;


    public boolean getLock(Integer timeout, TimeUnit timeUnit) {
        try {
            return redissonClient.getLock(TimeWheelConstant.TIME_WHEEL_LOCK).tryLock(timeout, timeUnit);
        } catch (InterruptedException e) {
            log.info("getLock fail ", e);
            return false;
        }
    }


    public List<TaskEntry> leftBatchPopTaskEntry(String key, Integer size) {
        List<TaskEntry> result = Collections.EMPTY_LIST;
        try {
            size = size > 500 ? 500 : size;
            RQueue<TaskEntry> queue = redissonClient.getQueue(key);
            result = queue.poll(size);
        } catch (Exception e) {
            log.error("leftBatchPopTaskEntry list operation error.{}", e.getMessage());
        }
        result.removeAll(Collections.singleton(null));
        return result;
    }


    public void rightPush(String key, TaskEntry taskEntry) {
        redissonClient.getQueue(key).add(taskEntry);
    }

    public Integer getSlotSize(String key) {
        return redissonClient.getQueue(key).size();
    }


    public Integer setSlot(String key, Integer value) {
        redissonClient.getBucket(key).set(value);
        return 1;
    }


    public Integer getSlot(String key) {
        return (Integer) redissonClient.getBucket(key).get();
    }

}
