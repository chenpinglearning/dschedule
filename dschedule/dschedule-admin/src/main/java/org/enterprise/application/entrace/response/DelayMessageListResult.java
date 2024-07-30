package org.enterprise.application.entrace.response;

import lombok.Data;
import org.enterprise.infrastructure.mysql.entity.DelayMessage;

import java.util.List;

@Data
public class DelayMessageListResult {
    private Long total;
    private Integer page;
    private List<DelayMessage> delayMessageResultList;
}
