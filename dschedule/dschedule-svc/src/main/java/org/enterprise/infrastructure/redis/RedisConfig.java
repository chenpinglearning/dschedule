package org.enterprise.infrastructure.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
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
    @Value("${redis.model}")
    private String redisMode;
    @Value("${redis.cluster.nodes}")
    private String nodes;
    @Value("${redis.cluster.password:#{null}}")
    private String password;


    @Bean
    public RedissonClient redissonClient() {
        List<String> clusterNodes = new ArrayList<>();
        String[] servers = nodes.split(",");
        for (String server : servers) {
            clusterNodes.add("redis://" + server);
        }

        Config config = new Config();
        switch (redisMode) {
            case "single":
                config.useSingleServer()
                        .setAddress(clusterNodes.get(0))
                        .setPassword(password)
                        .setConnectTimeout(1000)
                        .setTimeout(1000);
                break;
            case "cluster":
            default:
                config.useClusterServers()
                        .addNodeAddress(clusterNodes.toArray(new String[clusterNodes.size()]))
                        .setPassword(password)
                        .setConnectTimeout(1000)
                        .setTimeout(1000);
                break;
        }

        config.setCodec(new JsonJacksonCodec());
        return Redisson.create(config);
    }

}
