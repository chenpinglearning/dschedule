package org.enterprise.infrastructure.config.dto;

import lombok.Data;

/**
 * @author: albert.chen
 * @create: 2023-12-25
 * @description:
 */
@Data
public class RedisDelayMessagePool {
    private String appId;
    private String scene;
    private String pool;
    /**
     * windows_package_delay_message
     */
    private Integer windowsPackage;
}
