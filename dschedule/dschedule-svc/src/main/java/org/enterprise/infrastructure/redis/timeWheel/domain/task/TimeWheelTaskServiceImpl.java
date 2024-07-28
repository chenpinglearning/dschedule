package org.enterprise.infrastructure.redis.timeWheel.domain.task;

import lombok.extern.slf4j.Slf4j;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.domian.service.CallBackMessageManager;
import org.enterprise.infrastructure.redis.timeWheel.config.TimeWheelConfig;
import org.enterprise.infrastructure.redis.timeWheel.constants.TimeWheelConstant;
import org.enterprise.infrastructure.redis.timeWheel.dao.TimeWheelDao;
import org.enterprise.infrastructure.redis.timeWheel.entity.TaskEntry;
import org.enterprise.infrastructure.utils.thread.ThreadPools;
import org.enterprise.util.JacksonUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TimeWheelTaskServiceImpl implements TimeWheelTaskService {
    @Resource
    private TimeWheelDao timeWheelDao;
    @Resource
    private TimeWheelConfig timeWheelConfig;
    @Resource
    private CallBackMessageManager callBackMessageManager;

    @Override
    public void addTaskToWheel(TaskEntry taskEntry) {
        Integer currentSlot = getCurrentSlot(taskEntry.getScene());
        Integer targetSlot = (currentSlot + (taskEntry.getDelay() / taskEntry.getWindowPackage())) % timeWheelConfig.getSimpleSlotNum();
        taskEntry.setSlot(targetSlot);

        String key = TimeWheelConstant.TIME_WHEEL.concat(String.valueOf(targetSlot));
        log.info("addTaskToWheel key vale:{} {} ", key, JacksonUtil.obj2String(taskEntry));
        timeWheelDao.rightPush(key, taskEntry);
    }

    @Override
    public void turnTheTimeWheel(String scene) {
        Long start = System.currentTimeMillis();
        try {
            boolean isGet = timeWheelDao.getLock(TimeWheelConstant.TICK_DURATION, TimeUnit.MILLISECONDS);
            if (!isGet) {
                return;
            }

            Integer currentSlot = getCurrentSlot(scene);
            currentSlot = (currentSlot + 1) % timeWheelConfig.getSimpleSlotNum();

            timeWheelDao.setSlot(TimeWheelConstant.TIME_WHEEL, currentSlot);

            Long takeTime = System.currentTimeMillis() - start;
            log.debug("turn task time {}", takeTime);
        } catch (Exception e) {
            log.error("turnTheTimeWheel error.", e);
        }
    }

    @Override
    public void consumeTimeWheel(String scene) {
        Long start = System.currentTimeMillis();
        try {
            Integer slot = getCurrentSlot(scene);
            String key = TimeWheelConstant.TIME_WHEEL.concat(String.valueOf(slot));
            //槽位数量
            Integer slotSize = timeWheelDao.getSlotSize(key);
            if (slotSize == 0) {
                return;
            }
            //槽任务
            List<TaskEntry> taskEntryList = timeWheelDao.leftBatchPopTaskEntry(key, slotSize);
            if (CollectionUtils.isEmpty(taskEntryList)) {
                return;
            }

            log.info("consumeTimeWheel key->{}, size {} taskEntryList->{}", key, slotSize, JacksonUtil.obj2String(taskEntryList));

            Long consumeTime = System.currentTimeMillis();
            for (TaskEntry task : taskEntryList) {
                Long shouldConsumeTime = task.getCreatedAtTimeStamp() + task.getDelay();
                Long delay = consumeTime - shouldConsumeTime;
                if (delay > timeWheelConfig.getDelayConsume()) {
                    log.warn("consumeTimeWheel delay time {}", delay);
                }

                ThreadPools.timeWheelThreadPool.execute(() -> {
                    callBackMessageManager.callBackDelayMessage(JacksonUtil.string2Obj(task.getParams(), DscheduleRequest.class));
                });
            }

            Long takeTime = System.currentTimeMillis() - start;
            log.info("consume task time {}", takeTime);
        } catch (Exception e) {
            log.error("consumeTimeWheel error.", e);
        }
    }

    @Override
    public Integer getCurrentSlot(String scene) {
        Integer currentSlot = timeWheelDao.getSlot(TimeWheelConstant.TIME_WHEEL.concat(scene));
        if (currentSlot == null) {
            currentSlot = 0;
        }
        return currentSlot;
    }
}
