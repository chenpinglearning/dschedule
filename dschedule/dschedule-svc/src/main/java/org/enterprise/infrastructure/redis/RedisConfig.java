package org.enterprise.infrastructure.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

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
        String[] nodesList = nodes.split(",");
        List<String> nodeAddresses = new ArrayList<>();
        for (String address : nodesList) {
            nodeAddresses.add("redis://".concat(address));
        }

        Config config = new Config();
        /*config.useClusterServers()
                .setTimeout(1000)
                .setConnectTimeout(1000)
                .setPassword(password)
                .setReadMode(ReadMode.MASTER)
                .setNodeAddresses(nodeAddresses);*/

        config.useSingleServer()
                .setTimeout(1000)
                .setConnectTimeout(1000)
                .setPassword(password)
                .setAddress("redis://".concat(nodes));

        return Redisson.create(config);
    }

}
