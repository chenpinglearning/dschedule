package org.enterprise.infrastructure.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.enterprise.infrastructure.config.dto.RedisDelayMessagePool;
import org.enterprise.util.JacksonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: albert.chen
 * @create: 2023-12-25
 * @description:
 */
@Data
@Slf4j
@Component
public class BusinessConfig {
    @Value("${delay.message.demotion:false}")
    private Boolean delayMessageDemotion;

    private List<RedisDelayMessagePool> redisDelayMessagePool;

    @Value("${redis.delay.message.pool:[]}")
    public void setRedisDelayMessagePoolConfigList(String str) {
        try {
            this.redisDelayMessagePool = JacksonUtil.string2Collection(str, List.class, RedisDelayMessagePool.class);
        } catch (Exception e) {
            log.error("setRedisDelayMessagePoolConfigList parse error{}", str, e);
        } finally {
            log.info("setRedisDelayMessagePoolConfigList {}", str);
        }
    }

}
