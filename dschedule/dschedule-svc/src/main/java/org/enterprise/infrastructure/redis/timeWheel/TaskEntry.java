package org.enterprise.infrastructure.redis.timeWheel;

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
    private Integer delay;
    private Long createdAtTimeStamp;
}
