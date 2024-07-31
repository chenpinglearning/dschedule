package org.enterprise.application.entrace;

import lombok.extern.slf4j.Slf4j;
import org.enterprise.domain.service.DelayMessageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "/delay/message/retry/")
public class DelayMessageUpdateController {
    @Resource
    private DelayMessageService delayMessageService;

    @RequestMapping(value = "fail", method = RequestMethod.POST)
    public void retryFailedMessages(String seqId) {
         delayMessageService.retryFailedMessages(seqId);
    }


    @RequestMapping(value = "/all/fail", method = RequestMethod.POST)
    public void retryAllFailedMessages() {
        delayMessageService.retryAllFailedMessages();
    }


}
