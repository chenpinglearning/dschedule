package org.enterprise.domian.constants;

import org.enterprise.domian.service.DelayMessageCallBackService;
import org.enterprise.domian.service.HttpDelayMessageCallBack;
import org.enterprise.domian.service.KafkaDelayMessageCallBack;

/**
 * @author: albert.chen
 * @create: 2023-12-06
 * @description:
 */
public enum CallWayEnum {
    RABBITMQ(0, "http", new HttpDelayMessageCallBack()),
    KAFKA(1, "kafka", new KafkaDelayMessageCallBack());

    private Integer way;
    private String msg;
    private DelayMessageCallBackService delayMessageCallBackService;
    CallWayEnum(Integer way, String msg, DelayMessageCallBackService delayMessageCallBackService) {
        this.way = way;
        this.msg = msg;
        this.delayMessageCallBackService = delayMessageCallBackService;
    }

    public static DelayMessageCallBackService getDelayMessageCallBackService(String msg) {
        for (CallWayEnum value : CallWayEnum.values()) {
            if (value.getMsg().equals(msg)) {
                return value.delayMessageCallBackService;
            }
        }
        return RABBITMQ.delayMessageCallBackService;
    }

    public Integer getWay() {
        return way;
    }

    public String getMsg() {
        return msg;
    }


}
