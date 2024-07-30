package org.enterprise.domian.service;

import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.infrastructure.kafka.KafkaDelayAdapter;
import org.enterprise.infrastructure.utils.spring.SpringContextUtil;

/**
 * @author: albert.chen
 * @create: 2023-12-06
 * @description:
 */
public class KafkaDelayMessageCallBack extends DelayMessageCallBackService {


    @Override
    protected void callBackDelayMessage(DscheduleRequest dscheduleRequest) throws Exception {
        KafkaDelayAdapter kafkaDelayAdapter = SpringContextUtil.getBean(KafkaDelayAdapter.class);
        kafkaDelayAdapter.callBackDelayMessage(dscheduleRequest);
        super.callBackDelayMessage(dscheduleRequest);
    }

}
