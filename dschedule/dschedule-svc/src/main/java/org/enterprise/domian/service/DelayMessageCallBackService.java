package org.enterprise.domian.service;

import lombok.extern.slf4j.Slf4j;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.infrastructure.mysql.adapter.MysqlDelayAdapter;
import org.enterprise.infrastructure.utils.spring.SpringContextUtil;

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
            callBackDelayMessageFail(dscheduleRequest);
        } finally {
            afterCallBackDelayMessage(dscheduleRequest);
        }
    }


    protected DscheduleRequest beforeCallBackDelayMessage(DscheduleRequest dscheduleRequest) {
        log.info("callBack delay message start {}", dscheduleRequest.getSeqId());

        return dscheduleRequest;
    }


    protected void callBackDelayMessage(DscheduleRequest dscheduleRequest) throws Exception {
        log.info("callBack delay message success {}", dscheduleRequest.getSeqId());

        MysqlDelayAdapter mysqlDelayAdapter = SpringContextUtil.getBean(MysqlDelayAdapter.class);
        mysqlDelayAdapter.updateSuccessDelayMessage(dscheduleRequest.getSeqId());
    }

    protected void callBackDelayMessageFail(DscheduleRequest dscheduleRequest) throws Exception {
        log.error("callBack delay message fail {}", dscheduleRequest.getSeqId());

        MysqlDelayAdapter mysqlDelayAdapter = SpringContextUtil.getBean(MysqlDelayAdapter.class);
        mysqlDelayAdapter.updateFailDelayMessage(dscheduleRequest.getSeqId());
    }


    protected DscheduleRequest afterCallBackDelayMessage(DscheduleRequest dscheduleRequest) {
        log.info("callBack delay message end {}", dscheduleRequest.getSeqId());

        return dscheduleRequest;
    }
}
