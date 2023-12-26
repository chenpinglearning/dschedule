package org.enterprise.infrastructure.redis.timeWheel.job;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.enterprise.infrastructure.config.BusinessConfig;
import org.enterprise.infrastructure.config.dto.RedisDelayMessagePool;
import org.enterprise.infrastructure.redis.timeWheel.TimeWheelService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 **/
@Component
@Slf4j
public class TurnTimeWheelTask implements ApplicationRunner {
    @Resource
    private TimeWheelService timeWheelService;
    @Resource
    private BusinessConfig businessConfig;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<RedisDelayMessagePool> redisDelayMessagePool = businessConfig.getRedisDelayMessagePool();
        for (RedisDelayMessagePool delayMessagePool : redisDelayMessagePool) {
            // 从机器开启后，每100毫秒启动一次
            ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, new ThreadFactoryBuilder().setNameFormat("KhronosTimeWheel" + delayMessagePool.getPool()).build());
            scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
                        timeWheelService.turnTheTimeWheel(delayMessagePool);
                    }
                    , 5000, 50, TimeUnit.MILLISECONDS);
        }
    }
}
