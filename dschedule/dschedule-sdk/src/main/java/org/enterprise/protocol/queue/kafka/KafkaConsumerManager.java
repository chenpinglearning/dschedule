package org.enterprise.protocol.queue.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.enterprise.constants.EnvironmentConfig;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;


/**
 * @author: albert.chen
 * @create: 2023-11-29
 * @description:
 */

public class KafkaConsumerManager {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerManager.class);
    private static KafkaConsumer<String, String> kafkaConsumer = null;
    private static List<DscheduleKafkaConsumer> dscheduleKafkaConsumerList = new ArrayList<>();


    static {
        try {
            kafkaConsumer = new KafkaConsumer<>(initConsumerConfig());
            kafkaConsumer.subscribe(Collections.singletonList(EnvironmentConfig.KAFKA_REQUEST_TOPIC));

            Reflections reflections = new Reflections();
            Set<Class<? extends DscheduleKafkaConsumer>> subTypesOf = reflections.getSubTypesOf(DscheduleKafkaConsumer.class);
            for (Class<? extends DscheduleKafkaConsumer> subClass : subTypesOf) {
                DscheduleKafkaConsumer delayMessage = subClass.getConstructor().newInstance();
                dscheduleKafkaConsumerList.add(delayMessage);
            }
        } catch (Exception e) {
            logger.warn("init kafkaConsumer fail warn");
        }
    }

    public KafkaConsumerManager() {
        while (true) {
            try {
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    for (DscheduleKafkaConsumer dscheduleKafkaConsumer : dscheduleKafkaConsumerList) {
                        dscheduleKafkaConsumer.callBackMessage(consumerRecord.value());
                    }
                }
            } catch (Exception e) {
                logger.warn("poll delayMessage fail");
            }
        }
    }

    private static Properties initConsumerConfig() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, EnvironmentConfig.KAFKA_HOST);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, System.getProperty(EnvironmentConfig.APP_ID));
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.TRUE);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        return props;
    }

}

