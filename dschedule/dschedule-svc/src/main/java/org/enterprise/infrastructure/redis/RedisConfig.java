package org.enterprise.infrastructure.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author: albert.chen
 * @create: 2023-12-05
 * @description:
 */
@Configuration
@Conditional(value = RedisCondition.class)
public class RedisConfig {
    @Value("${redis.cluster.nodes}")
    private String nodes;
    @Value("${redis.cluster.password}")
    private String password;


    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useClusterServers()
                .setTimeout(1000)
                .setConnectTimeout(1000)
                .setPassword(password)
                .setReadMode(ReadMode.MASTER)
                .addNodeAddress(nodes);

        return Redisson.create(config);
    }

}
