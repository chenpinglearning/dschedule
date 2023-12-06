package org.enterprise.application.entrance;

import lombok.extern.slf4j.Slf4j;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.application.DelayMessageApplication;
import org.enterprise.infrastructure.utils.exception.BusinessException;
import org.enterprise.util.JacksonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
@Slf4j
@RestController("/delay/message/")
public class DelayMessageController {
    @Resource
    private DelayMessageApplication delayMessageApplication;


    @RequestMapping(value = "save", method = RequestMethod.POST)
    public void saveDelayMessage(@RequestParam String message) throws Exception {
        DscheduleRequest dscheduleRequest = JacksonUtil.string2Obj(message, DscheduleRequest.class);

        if (dscheduleRequest == null || dscheduleRequest.getAppId() == null || dscheduleRequest.getScene() == null
                || dscheduleRequest.getDelayTime() < System.currentTimeMillis() || dscheduleRequest.getDelayType() == null) {
            throw new BusinessException("param error");
        }

        delayMessageApplication.saveToDelayQueue(dscheduleRequest);
    }


}
