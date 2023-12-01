package org.enterprise.api;

import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.api.response.DscheduleResponse;
import org.enterprise.constants.DscheduleType;
import org.enterprise.constants.ProtocolType;
import org.enterprise.protocol.ProducerHandler;
import org.enterprise.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: albert.chen
 * @create: 2023-11-28
 * @description: Delayed schedule entrance
 */
public class DscheduleEventManage {
    public static final Logger logger = LoggerFactory.getLogger(DscheduleEventManage.class);
    private static final ThreadPoolExecutor httpExecutor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS
            , new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.AbortPolicy());


    public static void pushDelayMessage(DscheduleRequest dscheduleRequest) {
        httpExecutor.execute(() -> {
            startPushDelayMessage(dscheduleRequest);
        });
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

        if (dscheduleRequest.getProtocolType() == null) {
            dscheduleRequest.setProtocolType(ProtocolType.HTTP.getProtocol());
        }

        Map<String, Object> extraParam = dscheduleRequest.getExtraParam();
        extraParam.put("send_time", System.currentTimeMillis());

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
        if (dscheduleRequest == null || dscheduleRequest.getDelayTime() < System.currentTimeMillis()
                || dscheduleRequest.getAppid() == null || dscheduleRequest.getSeqId() == null) {
            return false;
        }

        return true;
    }


    protected static DscheduleRequest pushMessageBefore(DscheduleRequest dscheduleRequest) {
        return dscheduleRequest;
    }

    protected static DscheduleRequest pushMessageIng(DscheduleRequest dscheduleRequest) throws Exception {
        ProducerHandler producerHandler = ProtocolType.getCommunicationProtocol(dscheduleRequest.getProtocolType());
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
