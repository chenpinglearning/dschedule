package org.enterprise.infrastructure.kafka;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
@Conditional(value = KafkaCondition.class)
@Configuration
public class KafkaConfig {


}
