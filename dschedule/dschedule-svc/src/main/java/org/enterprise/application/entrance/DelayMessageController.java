package org.enterprise.application.entrance;

import lombok.extern.slf4j.Slf4j;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.application.DelayMessageApplication;
import org.enterprise.infrastructure.utils.exception.BusinessException;
import org.enterprise.util.JacksonUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
@Slf4j
@RestController
@RequestMapping(value = "/delay/message/")
public class DelayMessageController {
    @Resource
    private DelayMessageApplication delayMessageApplication;


    @RequestMapping(value = "save", method = RequestMethod.POST)
    public void saveDelayMessage(@RequestBody String message) throws Exception {
        DscheduleRequest dscheduleRequest = JacksonUtil.string2Obj(message, DscheduleRequest.class);

        log.info("saveDelayMessage request param {}", message);
        if (dscheduleRequest == null || dscheduleRequest.getAppId() == null || dscheduleRequest.getScene() == null
                || dscheduleRequest.getDelayTime() == null || dscheduleRequest.getDelayType() == null) {
            throw new BusinessException("param error");
        }

        delayMessageApplication.saveToDelayQueue(dscheduleRequest);
    }


}
