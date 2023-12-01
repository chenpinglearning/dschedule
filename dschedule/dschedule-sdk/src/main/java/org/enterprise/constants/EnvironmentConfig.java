package org.enterprise.constants;

/**
 * @author: albert.chen
 * @create: 2023-11-29
 * @description:
 */
public class EnvironmentConfig {
    //httpServer
    public static String HTTP_URL = "/dschedule/delay/message/request";
    public static String HTTP_HOST = "localhost:8080";

    //kafka-product
    public static String KAFKA_HOST = "localhost:9092";
    public static String KAFKA_REQUEST_TOPIC = "kafka-dschedule-delay-message-request-topic";
}
