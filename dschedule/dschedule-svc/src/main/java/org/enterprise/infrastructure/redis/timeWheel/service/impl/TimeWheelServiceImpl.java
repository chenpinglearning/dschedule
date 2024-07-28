package org.enterprise.infrastructure.redis.timeWheel.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.domian.service.CallBackMessageManager;
import org.enterprise.infrastructure.redis.timeWheel.config.TimeWheelConfig;
import org.enterprise.infrastructure.redis.timeWheel.constants.TimeWheelConstant;
import org.enterprise.infrastructure.redis.timeWheel.domain.task.TimeWheelTaskService;
import org.enterprise.infrastructure.redis.timeWheel.entity.TaskEntry;
import org.enterprise.infrastructure.redis.timeWheel.service.TimeWheelService;
import org.enterprise.util.JacksonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class TimeWheelServiceImpl implements TimeWheelService {
    @Resource
    private CallBackMessageManager callBackMessageManager;
    @Resource
    private TimeWheelTaskService timeWheelTaskService;
    @Resource
    private TimeWheelConfig timeWheelConfig;

    @Override
    public void addTask(DscheduleRequest dscheduleRequest) throws Exception {
        // windows_package use
        Integer windowsPackage = (Integer) dscheduleRequest.getExtraParam().get("windows_package");
        if (windowsPackage == null || !windowsPackage.equals(timeWheelConfig.getScenes().get(dscheduleRequest.getScene()))) {
            throw new Exception("scene windows_package is not config");
        }

        TaskEntry taskEntry = TaskEntry.builder()
                .params(JacksonUtil.obj2String(dscheduleRequest))
                .delay(dscheduleRequest.getDelayTime())
                .scene(dscheduleRequest.getScene())
                .windowPackage(windowsPackage)
                .createdAtTimeStamp(System.currentTimeMillis())
                .build();

        timeWheelTaskService.addTaskToWheel(taskEntry);
    }
}
