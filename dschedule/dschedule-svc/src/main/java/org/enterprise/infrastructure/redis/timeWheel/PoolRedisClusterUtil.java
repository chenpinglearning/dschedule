package org.enterprise.infrastructure.redis.timeWheel;

import lombok.extern.slf4j.Slf4j;
import org.enterprise.util.JacksonUtil;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PoolRedisClusterUtil {

    @Resource
    private RedissonClient redissonClient;


    public boolean getLock(String lockKey, Integer expireTime, TimeUnit timeUnit) {
        try {
            return redissonClient.getLock(lockKey).tryLock(expireTime, timeUnit);
        } catch (InterruptedException e) {
            log.error("get redis lock fail {} ", lockKey, e);
        }

        return false;
    }

    public Integer getCurrentPoint(String key) {
        Integer result = (Integer) redissonClient.getBucket(key).get();
        if (result == null) {
            return 0;
        }
        return result;
    }


    public void movePoint(String key, Integer value) {
        redissonClient.getBucket(key).set(value);
    }


    public int taskSizeInSlot(String key) {
        return redissonClient.getList(key).size();
    }


    public void pushTaskToSlot(String key, TaskEntry taskEntry) {
        redissonClient.getList(key).add(taskEntry);
    }


    public List<TaskEntry> getTaskFromSlot(String key) {
        try {
            List<Object> result = redissonClient.getQueue(key).poll(500);
            return result.stream().map(Object::toString).map(a -> JacksonUtil.string2Obj(a, TaskEntry.class)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("getTaskFromSlot error {}", key, e);
        }

        return null;
    }
}
