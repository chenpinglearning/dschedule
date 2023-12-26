package org.enterprise.infrastructure.redis.adapter;

import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.domian.service.CallBackMessageManager;
import org.enterprise.infrastructure.ProductAbstractDelayQueue;
import org.enterprise.infrastructure.config.BusinessConfig;
import org.enterprise.infrastructure.config.dto.RedisDelayMessagePool;
import org.enterprise.infrastructure.redis.timeWheel.TaskEntry;
import org.enterprise.infrastructure.redis.timeWheel.TimeWheelService;
import org.enterprise.infrastructure.redis.timeWheel.constants.KhronosBizConstant;
import org.enterprise.util.JacksonUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: albert.chen，window_package message to here
 * @create: 2023-12-05
 * @description:
 */
@Component
public class RedisDelayAdapter extends ProductAbstractDelayQueue {
    @Resource
    private TimeWheelService timeWheelService;
    @Resource
    private CallBackMessageManager callBackMessageManager;
    @Resource
    private BusinessConfig businessConfig;

    @Override
    public void sendDelayMessage(DscheduleRequest dscheduleRequest) {
        Integer delay = dscheduleRequest.getDelayTime();
        if (delay < KhronosBizConstant.TICK_DURATION) {
            callBackMessageManager.callBackDelayMessage(dscheduleRequest);
            return;
        }

        Map<String, Object> extraParam = dscheduleRequest.getExtraParam();
        Integer windowsPackageDelayTime = Optional.ofNullable(extraParam.get("windows_package_delay_time")).map(m -> Integer.parseInt((String) m)).orElse(null);

        List<RedisDelayMessagePool> redisDelayMessagePool = businessConfig.getRedisDelayMessagePool();
        RedisDelayMessagePool exitsConfig = redisDelayMessagePool.stream().filter((f) -> f.getAppId().equals(dscheduleRequest.getAppId())
                && f.getScene().equals(dscheduleRequest.getScene())).collect(Collectors.toList()).get(0);
        String actionPool = exitsConfig.getPool();
        Integer currentSlot = timeWheelService.getCurrentSlot(actionPool);
        Integer targetSlot = 0;
        if (windowsPackageDelayTime != null && windowsPackageDelayTime > 0) {
            //windows_package message deal
            targetSlot = (currentSlot + (windowsPackageDelayTime / exitsConfig.getWindowsPackage())) % KhronosBizConstant.WINDOWS_PACKAGE_SIMPLE_SLOT_NUM;
        } else {
            //real time message
            targetSlot = (currentSlot + (delay / KhronosBizConstant.TICK_DURATION)) % KhronosBizConstant.SIMPLE_SLOT_NUM;
        }

        TaskEntry taskEntry = TaskEntry.builder()
                .slot(targetSlot)
                .params(JacksonUtil.obj2String(dscheduleRequest))
                .delay(delay)
                .createdAtTimeStamp(System.currentTimeMillis())
                .build();
        timeWheelService.addTaskToWheel(taskEntry, actionPool);
    }


    public void callBackDelayMessage(List<TaskEntry> taskEntryList) {
        for (TaskEntry taskEntry : taskEntryList) {
            DscheduleRequest dscheduleRequest = JacksonUtil.string2Obj(taskEntry.getParams(), DscheduleRequest.class);
            callBackMessageManager.callBackDelayMessage(dscheduleRequest);
        }
    }

}
