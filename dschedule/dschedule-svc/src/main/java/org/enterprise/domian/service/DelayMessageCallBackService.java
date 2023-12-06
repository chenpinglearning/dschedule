package org.enterprise.domian.service;

import lombok.extern.slf4j.Slf4j;
import org.enterprise.api.request.DscheduleRequest;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
@Slf4j
public class DelayMessageCallBackService {


    public void callBack(DscheduleRequest dscheduleRequest) throws Exception {
        try {
            beforeCallBackDelayMessage(dscheduleRequest);

            callBackDelayMessage(dscheduleRequest);
        } catch (Exception e) {
            log.error("回掉延迟消息失败，原因为 {}", dscheduleRequest.getSeqId(), e);

            throw e;
        } finally {
            afterCallBackDelayMessage(dscheduleRequest);
        }
    }


    protected DscheduleRequest beforeCallBackDelayMessage(DscheduleRequest dscheduleRequest) {
        log.info("callBack delay message start {}", dscheduleRequest.getSeqId());

        return dscheduleRequest;
    }


    protected void callBackDelayMessage(DscheduleRequest dscheduleRequest) throws Exception {

    }


    protected DscheduleRequest afterCallBackDelayMessage(DscheduleRequest dscheduleRequest) {
        log.info("callBack delay message end {}", dscheduleRequest.getSeqId());

        return dscheduleRequest;
    }
}
