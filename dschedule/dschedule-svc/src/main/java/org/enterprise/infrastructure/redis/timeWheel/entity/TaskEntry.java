package org.enterprise.infrastructure.redis.timeWheel.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntry {
    private Integer slot;
    private String params;
    private String scene;
    private Integer delay;
    private Integer windowPackage;
    private Long createdAtTimeStamp;
}
