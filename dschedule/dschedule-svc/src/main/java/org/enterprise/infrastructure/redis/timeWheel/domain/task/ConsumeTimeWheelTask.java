package org.enterprise.infrastructure.redis.timeWheel.domain.task;

import lombok.extern.slf4j.Slf4j;
import org.enterprise.infrastructure.redis.timeWheel.config.TimeWheelConfig;
import org.enterprise.infrastructure.utils.spring.SpringContextUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class ConsumeTimeWheelTask implements ApplicationRunner {

    @Resource
    private TimeWheelTaskService timeWheelTaskService;

    @Value("${time.wheel.consume.time.wheel.initial.delay:6}")
    private Long initialDelay;

    @Value("${time.wheel.consume.time.wheel.period:20}")
    private Long period;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        TimeWheelConfig timeWheelConfig = SpringContextUtil.getBean(TimeWheelConfig.class);
        for (Map.Entry<String, Integer> scene : timeWheelConfig.getScenes().entrySet()) {
            ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
            scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
                        timeWheelTaskService.consumeTimeWheel(scene.getKey());
                    }
                    , initialDelay, scene.getValue(), TimeUnit.MILLISECONDS);
        }
    }
}
