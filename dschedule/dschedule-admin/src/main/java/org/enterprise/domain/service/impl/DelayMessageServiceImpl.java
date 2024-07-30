package org.enterprise.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.enterprise.application.entrace.request.QueryDelayMessageRequest;
import org.enterprise.application.entrace.response.DelayMessageListResult;
import org.enterprise.domain.service.DelayMessageService;
import org.enterprise.infrastructure.mysql.adapter.MysqlDelayAdapter;
import org.enterprise.infrastructure.mysql.entity.DelayMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
