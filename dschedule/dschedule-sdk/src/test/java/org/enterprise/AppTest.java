package org.enterprise;

import org.enterprise.api.DscheduleEventManage;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.constants.DscheduleType;
import org.enterprise.constants.EnvironmentConfig;
import org.enterprise.constants.ProtocolType;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * Unit test for simple App.
 */
public class AppTest {


    /**
     * Rigorous Test :-)
     */
    @Test
    public void httpNormalDelayMessage() {
        Properties properties = System.getProperties();
        properties.put(EnvironmentConfig.HTTP_HOST, "https://localhost:8888");

        DscheduleRequest dscheduleRequest = new DscheduleRequest();
        dscheduleRequest.setDelayTime(System.currentTimeMillis() + 10 * 1000);
        dscheduleRequest.setDelayType(DscheduleType.RABBITMQ.getType());
        dscheduleRequest.setProtocolType(ProtocolType.HTTP.getProtocol());
        dscheduleRequest.setAppid("you system name");
        dscheduleRequest.setSeqId("can search delay seqId");
        dscheduleRequest.setScene("you business scene");

        Map<String, Object> param = new HashMap<>();
        param.put("other", "other");
        param.put("business_id", "can search delay businessId");
        dscheduleRequest.setParam(param);

        Map<String, Object> extraParam = new HashMap<>();
        param.put("call_back_url", "https://localhost:8080/delay/call/back");
        param.put("grey_version", "v5337");
        dscheduleRequest.setExtraParam(extraParam);

        DscheduleEventManage.pushDelayMessage(dscheduleRequest);

        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
