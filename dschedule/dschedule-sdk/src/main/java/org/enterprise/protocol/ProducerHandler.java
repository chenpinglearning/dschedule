package org.enterprise.protocol;

/**
 * @author: albert.chen
 * @create: 2023-11-28
 * @description:
 */
public interface ProducerHandler {
    void sendDelayMessage(String delayMessage) throws Exception;
}
