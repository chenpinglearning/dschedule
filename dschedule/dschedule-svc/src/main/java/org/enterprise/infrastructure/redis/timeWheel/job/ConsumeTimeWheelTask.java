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
 * @Description
 * @Author zhuangyuyang
 * @Date 2023/1/16 18:08
 **/
@Component
@Slf4j
public class ConsumeTimeWheelTask implements ApplicationRunner {
    @Resource
    private TimeWheelService timeWheelService;
    @Resource
    private BusinessConfig businessConfig;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<RedisDelayMessagePool> redisDelayMessagePool = businessConfig.getRedisDelayMessagePool();
        for (RedisDelayMessagePool delayMessagePool : redisDelayMessagePool) {
            // 从机器开启后，每20毫秒启动一次
            ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, new ThreadFactoryBuilder().setNameFormat("consumeKhronosTimeWheel" + delayMessagePool.getPool()).build());
            scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
                        timeWheelService.consumeTimeWheel(delayMessagePool);
                    }
                    , 5000, 20, TimeUnit.MILLISECONDS);
        }
    }
}
