package org.enterprise.constants;

/**
 * @author: albert.chen
 * @create: 2023-11-29
 * @description:
 */
public class EnvironmentConfig {
    //httpServer
    public static String HTTP_URL = "/dschedule/delay/message/save";
    public static String HTTP_HOST = "localhost:8080";

    //kafka-delay-message-meta
    public static String KAFKA_HOST = "localhost:9092";
    public static String KAFKA_REQUEST_TOPIC = "kafka-dschedule-delay-message-request-topic";


    //kafka-consumer-group
    public static String APP_ID = "app.id";
}
