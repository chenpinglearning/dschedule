package org.enterprise.infrastructure.redis.timeWheel.domain.task;


import org.enterprise.infrastructure.redis.timeWheel.entity.TaskEntry;

public interface TimeWheelTaskService {

    void addTaskToWheel(TaskEntry taskEntry);

    void turnTheTimeWheel(String scene);

    void consumeTimeWheel(String scene);

    Integer getCurrentSlot(String scene);

}
