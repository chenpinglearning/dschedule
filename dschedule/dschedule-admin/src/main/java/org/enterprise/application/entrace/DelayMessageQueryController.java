package org.enterprise.application.entrace;

import lombok.extern.slf4j.Slf4j;
import org.enterprise.application.entrace.request.QueryDelayMessageRequest;
import org.enterprise.application.entrace.response.DelayMessageListResult;
import org.enterprise.domain.service.DelayMessageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "/delay/message/query/")
public class DelayMessageQueryController {
    @Resource
    private DelayMessageService delayMessageService;

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public DelayMessageListResult queryDelayMessageLists(QueryDelayMessageRequest queryDelayMessageRequest) {
        return delayMessageService.queryDealMessagesLists(queryDelayMessageRequest);
    }

}
