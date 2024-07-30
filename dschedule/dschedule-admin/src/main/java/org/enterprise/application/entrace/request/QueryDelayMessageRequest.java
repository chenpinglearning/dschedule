package org.enterprise.application.entrace.request;

import lombok.Data;

@Data
public class QueryDelayMessageRequest {
    private Integer status;
    private String seqId;
    private String scene;
    private String appId;
    private Integer page;
    private Integer size;
}
