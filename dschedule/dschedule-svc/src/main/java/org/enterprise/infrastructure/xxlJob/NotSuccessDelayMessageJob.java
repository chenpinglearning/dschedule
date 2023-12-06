package org.enterprise.infrastructure.xxlJob;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: albert.chen
 * @create: 2023-12-05
 * @description:
 */
@Slf4j
@Component
public class NotSuccessDelayMessageJob {

    /**
     * retry fail message, must serial
     *
     * @throws Exception
     */
    @XxlJob("notSuccessDelayMessageJobHandler")
    public void notSuccessDelayMessageJobHandler() throws Exception {
        log.info("not success delay message handler");

    }
}
