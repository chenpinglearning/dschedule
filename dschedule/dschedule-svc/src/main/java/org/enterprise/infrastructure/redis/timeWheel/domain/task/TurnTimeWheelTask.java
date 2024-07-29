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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class TurnTimeWheelTask implements ApplicationRunner {

    @Resource
    private TimeWheelTaskService timeWheelTaskService;

    @Value("${time.wheel.turn.time.initial.delay:6}")
    private Long initialDelay;

    @Value("${time.wheel.turn.time.period:50}")
    private Long period;

    private Map<String , Boolean> start = new ConcurrentHashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
                    startTurnTimeWheel();
                }
                , initialDelay, 1000, TimeUnit.MILLISECONDS);
    }

    private void startTurnTimeWheel() {
        TimeWheelConfig timeWheelConfig = SpringContextUtil.getBean(TimeWheelConfig.class);
        for (Map.Entry<String, Integer> scene : timeWheelConfig.getScenes().entrySet()) {
            Boolean old = start.putIfAbsent(scene.getKey(), true);
            if(old != null) {
                continue;
            }

            log.info("startTurnTimeWheel:{}", scene.getKey());
            ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
            scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
                        timeWheelTaskService.turnTheTimeWheel(scene.getKey());
                    }
                    , initialDelay, scene.getValue(), TimeUnit.MILLISECONDS);
        }
    }
}
