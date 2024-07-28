package org.enterprise.application;

import lombok.extern.slf4j.Slf4j;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.domian.constants.DelayDealWayEnum;
import org.enterprise.infrastructure.ProductAbstractDelayQueue;
import org.enterprise.infrastructure.mysql.adapter.MysqlDelayAdapter;
import org.enterprise.infrastructure.utils.spring.SpringContextUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
@Slf4j
@Component
public class DelayMessageApplication {
    @Resource
    private MysqlDelayAdapter delayMessageMysqlAdapter;


    public void saveToDelayQueue(DscheduleRequest dscheduleRequest) throws Exception {
        log.info("save delay message to queue {}", dscheduleRequest.getSeqId());
        /**
         * qps very high need deal, for example ,don't save to mysql
         */
        if (!delayMessageMysqlAdapter.save(dscheduleRequest)) {
            return;
        }

        //send to delay queue
        Integer delayType = dscheduleRequest.getDelayType();
        ProductAbstractDelayQueue productAbstractDelayQueue = (ProductAbstractDelayQueue) SpringContextUtil.getBean(DelayDealWayEnum.getAbstractDelayQueue(delayType));
        productAbstractDelayQueue.sendDelayMessage(dscheduleRequest);
    }


}
