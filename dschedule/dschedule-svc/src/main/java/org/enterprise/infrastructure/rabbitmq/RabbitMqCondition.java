package org.enterprise.infrastructure.rabbitmq;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
@Component
public class RabbitMqCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        return Boolean.parseBoolean(environment.getProperty("open.rabbitMq"));
    }


}
