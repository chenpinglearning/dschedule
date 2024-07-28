package org.enterprise.infrastructure.xxlJob;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.domian.entity.DelayMessage;
import org.enterprise.domian.service.CallBackMessageManager;
import org.enterprise.infrastructure.mysql.adapter.MysqlDelayAdapter;
import org.enterprise.util.JacksonUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
@Slf4j
@Component
public class RetryDelayMessageJob {
    @Resource
    private MysqlDelayAdapter delayMessageMysqlAdapter;
    @Resource
    private CallBackMessageManager callBackMessageManager;


    /**
     * retry fail message, must serial
     *
     * @throws Exception
     */
    @XxlJob("retryDelayMessageJobHandler")
    public void retryDelayMessageJobHandler() throws Exception {
        log.info("retryDelayMessageJobHandler start");
        String jobParam = XxlJobHelper.getJobParam();
        Integer size = Optional.ofNullable(jobParam).map(Integer::parseInt).orElse(50);

        List<DelayMessage> delayMessages = delayMessageMysqlAdapter.queryDealFailDelayMessages(size);
        if (CollectionUtils.isEmpty(delayMessages)) {
            log.info("not search fail delay message");
            return;
        }

        List<DscheduleRequest> dscheduleRequests = tranDscheduleRequests(delayMessages);
        for (DscheduleRequest dscheduleRequest : dscheduleRequests) {
            callBackMessageManager.callBackDelayMessage(dscheduleRequest);
        }
    }


    private List<DscheduleRequest> tranDscheduleRequests(List<DelayMessage> delayMessages) {
        List<DscheduleRequest> list = new ArrayList<>();
        for (DelayMessage delayMessage : delayMessages) {
            DscheduleRequest dscheduleRequest = new DscheduleRequest();
            dscheduleRequest.setDelayTime(delayMessage.getDelayTime());
            dscheduleRequest.setDelayType(delayMessage.getDelayType());

            dscheduleRequest.setAppId(delayMessage.getAppId());
            dscheduleRequest.setSeqId(delayMessage.getSeqId());
            dscheduleRequest.setParam(JacksonUtil.string2Obj(delayMessage.getBusinessParam(), Map.class));
            dscheduleRequest.setExtraParam(JacksonUtil.string2Obj(delayMessage.getExtraParam(), Map.class));
            dscheduleRequest.setScene(delayMessage.getScene());

            list.add(dscheduleRequest);
        }

        return list;
    }

}
