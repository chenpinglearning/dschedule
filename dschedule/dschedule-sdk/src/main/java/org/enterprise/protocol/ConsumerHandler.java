package org.enterprise.protocol;

/**
 * @author: albert.chen
 * @create: 2023-12-06
 * @description:
 */
public interface ConsumerHandler {
    void callBackMessage(String message);
}
