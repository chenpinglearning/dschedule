package org.enterprise.protocol.queue.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.enterprise.constants.EnvironmentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


/**
 * @author: albert.chen
 * @create: 2023-11-29
 * @description:
 */

public class KafkaProducerManager {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerManager.class);
    private static final KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(initProductConfig());


    private static Properties initProductConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, EnvironmentConfig.KAFKA_HOST);
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        return props;
    }


    public static void sendMessage(String message) {
        ProducerRecord<String, String> producer = new ProducerRecord<>(EnvironmentConfig.KAFKA_REQUEST_TOPIC, message);
        kafkaProducer.send(producer, (metadata, e) -> {
            if (e != null) {
                logger.error("send message error ,topic request {} {}", metadata.topic(), message, e);
            }
        });
    }

}

