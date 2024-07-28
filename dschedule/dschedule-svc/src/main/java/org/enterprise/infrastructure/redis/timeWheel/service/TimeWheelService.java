package org.enterprise.infrastructure.redis.timeWheel.service;

import org.enterprise.api.request.DscheduleRequest;

public interface TimeWheelService {

    void addTask(DscheduleRequest dscheduleRequest) throws Exception;

}
