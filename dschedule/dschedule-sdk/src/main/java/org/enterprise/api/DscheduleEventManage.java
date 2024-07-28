package org.enterprise.api;

import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.api.response.DscheduleResponse;
import org.enterprise.constants.Constant;
import org.enterprise.constants.DscheduleType;
import org.enterprise.constants.ProtocolType;
import org.enterprise.protocol.ProducerHandler;
import org.enterprise.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author: albert.chen
 * @create: 2023-11-28
 * @description: Delayed schedule entrance
 */
public class DscheduleEventManage {
    public static final Logger logger = LoggerFactory.getLogger(DscheduleEventManage.class);


    public static DscheduleResponse pushDelayMessage(DscheduleRequest dscheduleRequest) {
        return startPushDelayMessage(dscheduleRequest);
    }


    /**
     * push delay message
     *
     * @param dscheduleRequest
     * @return
     */
    private static DscheduleResponse startPushDelayMessage(DscheduleRequest dscheduleRequest) {
        // checkParam
        if (!checkParam(dscheduleRequest)) {
            logger.error("param not valid {}", dscheduleRequest.toString());
            return DscheduleResponse.paramNotValid();
        }
        logger.info("push message request {}", dscheduleRequest.getSeqId());
        if (dscheduleRequest.getDelayType() == null) {
            dscheduleRequest.setDelayType(DscheduleType.RABBITMQ.getType());
        }

        Map<String, Object> extraParam = dscheduleRequest.getExtraParam();
        extraParam.put(Constant.SendTime, System.currentTimeMillis());

        // before sendMessage
        pushMessageBefore(dscheduleRequest);

        try {
            // push
            pushMessageIng(dscheduleRequest);

            // push after
            pushMessageAfter(dscheduleRequest);

            return DscheduleResponse.success();
        } catch (Exception e) {
            logger.error("push delay message exception {}", dscheduleRequest.getSeqId(), e);
            // push exception
            pushMessageException(dscheduleRequest);
            return DscheduleResponse.fail();
        }
    }


    private static boolean checkParam(DscheduleRequest dscheduleRequest) {
        return dscheduleRequest != null && dscheduleRequest.getDelayTime() != null
                && dscheduleRequest.getAppId() != null && dscheduleRequest.getSeqId() != null
                && dscheduleRequest.getExtraParam() != null && dscheduleRequest.getExtraParam().get(Constant.CallBackUrl) != null
                && (((String) dscheduleRequest.getExtraParam().get(Constant.CallBackUrl)).contains(Constant.http)
                || ((String) dscheduleRequest.getExtraParam().get(Constant.CallBackUrl)).contains(Constant.kafka));
    }


    protected static DscheduleRequest pushMessageBefore(DscheduleRequest dscheduleRequest) {
        return dscheduleRequest;
    }

    protected static DscheduleRequest pushMessageIng(DscheduleRequest dscheduleRequest) throws Exception {
        ProducerHandler producerHandler = ProtocolType.getCommunicationProtocol(ProtocolType.HTTP.getProtocol());
        if (producerHandler == null) {
            throw new Exception("not found communicationProtocol");
        }
        // send success(mean not exception)
        producerHandler.sendDelayMessage(JacksonUtil.obj2String(dscheduleRequest));
        return dscheduleRequest;
    }

    protected static DscheduleRequest pushMessageAfter(DscheduleRequest dscheduleRequest) {
        logger.info("push success ,seqId {}", dscheduleRequest.getSeqId());
        return dscheduleRequest;
    }

    protected static DscheduleRequest pushMessageException(DscheduleRequest dscheduleRequest) {
        return dscheduleRequest;
    }
}
