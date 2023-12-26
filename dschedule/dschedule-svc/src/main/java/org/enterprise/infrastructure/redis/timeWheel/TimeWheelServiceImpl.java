package org.enterprise.infrastructure.redis.timeWheel;

import lombok.extern.slf4j.Slf4j;
import org.enterprise.infrastructure.config.dto.RedisDelayMessagePool;
import org.enterprise.infrastructure.monitor.MonitorUtil;
import org.enterprise.infrastructure.redis.adapter.RedisDelayAdapter;
import org.enterprise.infrastructure.redis.timeWheel.constants.KhronosBizConstant;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author zhuangyuyang
 * @Date 2023/1/6 14:02
 **/
@Service
@Slf4j
public class TimeWheelServiceImpl implements TimeWheelService {
    @Resource
    private PoolRedisClusterUtil poolRedisClusterUtil;
    @Resource
    private RedisDelayAdapter redisDelayAdapter;


    @Override
    public void addTaskToWheel(TaskEntry taskEntries, String actionPool) {
        String key = KhronosBizConstant.TIME_WHEEL_SLOT;
        key = key.concat(actionPool).concat(KhronosBizConstant.SPLIT).concat(String.valueOf(taskEntries.getSlot()));
        poolRedisClusterUtil.pushTaskToSlot(key, taskEntries);
        log.info("TimeWheelServiceImpl->addTaskToWheel->{} ->{}", key, taskEntries);
        // 监控
        try {
            MonitorUtil.getKhronosTimeWheelInTask().labels(actionPool).inc();
        } catch (Exception e) {
            log.error("addTaskToWheel MonitorUtil Exception.", e);
        }
    }


    @Override
    public void turnTheTimeWheel(RedisDelayMessagePool delayMessagePool) {
        Long start = System.currentTimeMillis();
        MDC.put("REQUEST_ID", start.toString());
        try {
            //lock
            boolean isGet = poolRedisClusterUtil.getLock(KhronosBizConstant.TURN_WHEEL_BLOCK_REDIS_KEY
                    .concat(KhronosBizConstant.SPLIT).concat(delayMessagePool.getPool()), KhronosBizConstant.TICK_DURATION, TimeUnit.MILLISECONDS);
            if (isGet) {
                turnTimeWheel(delayMessagePool);
                Long takeTime = System.currentTimeMillis() - start;
                log.info("turnTheTimeWheel end take ms {}, actionEnum {}", takeTime, delayMessagePool.getPool());
                MonitorUtil.getKhronosTurnTakeTimeCount().labels(delayMessagePool.getPool()).set(takeTime);
            }
        } catch (Exception e) {
            log.error("turnTheTimeWheel error.", e);
        } finally {
            MDC.remove("REQUEST_ID");
        }
    }

    @Override
    public void consumeTimeWheel(RedisDelayMessagePool delayMessagePool) {
        Long start = System.currentTimeMillis();
        MDC.put("REQUEST_ID", start.toString());
        try {
            Integer slot = getCurrentSlot(delayMessagePool.getPool());
            String key = KhronosBizConstant.TIME_WHEEL_SLOT.concat(delayMessagePool.getPool()).concat(KhronosBizConstant.SPLIT).concat(String.valueOf(slot));
            int slotSize = poolRedisClusterUtil.taskSizeInSlot(key);
            if (slotSize == 0) {
                return;
            }
            List<TaskEntry> taskEntryList = poolRedisClusterUtil.getTaskFromSlot(key);
            log.info("consumeTimeWheel key->{}, taskEntryList->{}, actionEnum->{}", key, taskEntryList, delayMessagePool.getPool());
            if (CollectionUtils.isEmpty(taskEntryList)) {
                return;
            }
            MonitorUtil.getKhronosTimeWheelTaskCount().labels(delayMessagePool.getPool()).set(slotSize);
            Long consumeTime = System.currentTimeMillis();
            for (TaskEntry task : taskEntryList) {
                // 任务理应消费的时间戳
                Long shouldConsumeTime = task.getCreatedAtTimeStamp() + task.getDelay();
                // 若延误的时间达到阈值，则上报延误
                Long delay = consumeTime - shouldConsumeTime;
                if (delay > KhronosBizConstant.TICK_DURATION) {
                    MonitorUtil.getKhronosTimeWheelDelayTime().labels(delayMessagePool.getPool()).set(delay);
                }
            }

            redisDelayAdapter.callBackDelayMessage(taskEntryList);
        } catch (Exception e) {
            log.error("consumeTimeWheel error.", e);
        } finally {
            MDC.remove("REQUEST_ID");
        }
    }


    @Override
    public Integer getCurrentSlot(String actionPool) {
        String key = KhronosBizConstant.TIME_WHEEL_CURRENT_POINT.concat(KhronosBizConstant.SPLIT).concat(actionPool);
        return poolRedisClusterUtil.getCurrentPoint(key);
    }


    private void turnTimeWheel(RedisDelayMessagePool delayMessagePool) {
        try {
            String key = KhronosBizConstant.TIME_WHEEL_CURRENT_POINT.concat(KhronosBizConstant.SPLIT).concat(delayMessagePool.getPool());
            Integer currentSlot = poolRedisClusterUtil.getCurrentPoint(key);
            log.info("turnTheTimeWheel getCurrentSlot.->{}", delayMessagePool.getPool());
            int slotNum = KhronosBizConstant.SIMPLE_SLOT_NUM;
            if (delayMessagePool.getWindowsPackage() != null) {
                slotNum = delayMessagePool.getWindowsPackage();
            }

            currentSlot = (currentSlot + 1) % slotNum;
            poolRedisClusterUtil.movePoint(key, currentSlot);
        } catch (Exception e) {
            MonitorUtil.getKhronosTurnTimeErrorCount().labels(delayMessagePool.getPool()).inc();
        }
    }
}
