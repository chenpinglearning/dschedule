package org.enterprise.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.enterprise.api.DscheduleEventManage;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.application.entrace.request.QueryDelayMessageRequest;
import org.enterprise.application.entrace.response.DelayMessageListResult;
import org.enterprise.domain.service.DelayMessageService;
import org.enterprise.infrastructure.mysql.adapter.MysqlDelayAdapter;
import org.enterprise.infrastructure.mysql.entity.DelayMessage;
import org.enterprise.util.JacksonUtil;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

@Slf4j
@Service
public class DelayMessageServiceImpl implements DelayMessageService {
    @Resource
    private MysqlDelayAdapter mysqlDelayAdapter;
    @Resource
    private KafkaTemplate kafkaTemplate;


    @Override
    public DelayMessageListResult queryDealMessagesLists(QueryDelayMessageRequest request) {
        Page<DelayMessage> delayMessagePage = mysqlDelayAdapter.queryDealMessagesPage(request);
        DelayMessageListResult delayMessageListResult = new DelayMessageListResult();
        delayMessageListResult.setDelayMessageResultList(delayMessagePage.getRecords());
        delayMessageListResult.setTotal(delayMessagePage.getTotal());
        delayMessageListResult.setPage(request.getPage());
        return delayMessageListResult;
    }


    @Override
    public void retryFailedMessages(String seqId) {
        DelayMessage delayMessage = mysqlDelayAdapter.queryDealMessagesBySeqId(seqId);
        DscheduleEventManage.pushDelayMessage(JacksonUtil.string2Obj(JacksonUtil.obj2String(delayMessage) , DscheduleRequest.class));
    }


    @Override
    public void retryAllFailedMessages() {
        QueryDelayMessageRequest request = new QueryDelayMessageRequest();
        request.setStatus(-1);
        request.setPage(0);
        request.setSize(100);
        while (true) {
            request.setPage(request.getPage() + 1);
            Page<DelayMessage> delayMessagePage = mysqlDelayAdapter.queryDealMessagesPage(request);
            if (CollectionUtils.isEmpty(delayMessagePage.getRecords())) {
                return;
            }
            for (DelayMessage delayMessage : delayMessagePage.getRecords()) {
                log.info("retryAllFailedMessages delayMessage {}" , JacksonUtil.obj2String(delayMessage));
                DscheduleEventManage.pushDelayMessage(JacksonUtil.string2Obj(JacksonUtil.obj2String(delayMessage) , DscheduleRequest.class));
            }
        }

    }
}
