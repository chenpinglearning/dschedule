package org.enterprise.infrastructure.rabbitmq;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
public enum ExchangeEnum {
    DELAY_EXCHANGE("dschedule.deply.message.exchange");

    private String value;

    ExchangeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
