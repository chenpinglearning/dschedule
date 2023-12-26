package org.enterprise.infrastructure.xxlJob;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.domian.entity.DelayMessage;
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
public class StartDelayMessageJob {
    @Resource
    private MysqlDelayAdapter delayMessageMysqlAdapter;


    /**
     * user mysql delay message, must serial
     *
     * @throws Exception
     */
    @XxlJob("startDelayMessageJobHandler")
    public void startDelayMessageJobHandler() throws Exception {
        log.info("delayMessageJobHandler start");
        String jobParam = XxlJobHelper.getJobParam();
        Integer size = Optional.ofNullable(jobParam).map(Integer::parseInt).orElse(50);

        List<DelayMessage> delayMessages = delayMessageMysqlAdapter.queryNotDealDelayMessages(size);
        if (CollectionUtils.isEmpty(delayMessages)) {
            log.info("not search delay message");
            return;
        }

        List<DscheduleRequest> dscheduleRequests = tranDscheduleRequests(delayMessages);
    }


    private List<DscheduleRequest> tranDscheduleRequests(List<DelayMessage> delayMessages) {
        List<DscheduleRequest> list = new ArrayList<>();
        for (DelayMessage delayMessage : delayMessages) {
            DscheduleRequest dscheduleRequest = new DscheduleRequest();
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
