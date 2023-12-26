package org.enterprise.infrastructure.redis.timeWheel;


import org.enterprise.infrastructure.config.dto.RedisDelayMessagePool;

/**
 *
 **/
public interface TimeWheelService {

    /**
     * addTaskToWheel
     */
    void addTaskToWheel(TaskEntry taskEntry, String actionPool);


    /**
     * turnTheTimeWheel
     */
    void turnTheTimeWheel(RedisDelayMessagePool delayMessagePool);

    /**
     * consumeTimeWheel
     */
    void consumeTimeWheel(RedisDelayMessagePool delayMessagePool);


    /**
     * actionPool
     */
    Integer getCurrentSlot(String actionPool);
}
