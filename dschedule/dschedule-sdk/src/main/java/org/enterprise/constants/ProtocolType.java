package org.enterprise.constants;

import org.enterprise.protocol.ProducerHandler;
import org.enterprise.protocol.http.DscheduleHttpProducer;
import org.enterprise.protocol.queue.kafka.DscheduleKafkaProducer;

/**
 * @author: albert.chen
 * @create: 2023-11-28
 * @description:
 */
public enum ProtocolType {
    HTTP(0, "http", new DscheduleHttpProducer()),
    KAFKA(0, "kafka", new DscheduleKafkaProducer()),
    ;


    private int protocol;
    private String description;
    private ProducerHandler producerHandler;

    ProtocolType(int protocol, String description, ProducerHandler producerHandler) {
        this.protocol = protocol;
        this.description = description;
        this.producerHandler = producerHandler;
    }

    public static ProducerHandler getCommunicationProtocol(int protocolType) {
        for (ProtocolType value : ProtocolType.values()) {
            if (value.protocol == protocolType) {
                return value.producerHandler;
            }
        }

        return null;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProducerHandler(ProducerHandler producerHandler) {
        this.producerHandler = producerHandler;
    }


}
