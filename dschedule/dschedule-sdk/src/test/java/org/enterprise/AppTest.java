package org.enterprise;

import org.enterprise.api.DscheduleEventManage;
import org.enterprise.api.request.DscheduleRequest;
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
        dscheduleRequest.setDelayTime(10000);
        dscheduleRequest.setProtocolType(ProtocolType.HTTP.getProtocol());
        dscheduleRequest.setAppId("test");
        dscheduleRequest.setSeqId(String.valueOf(System.currentTimeMillis()));
        dscheduleRequest.setScene("default");

        Map<String, Object> param = new HashMap<>();
        param.put("other", "other");
        param.put("business_id", "123123213123123");
        dscheduleRequest.setParam(param);

        Map<String, Object> extraParam = new HashMap<>();
        param.put("call_back_url", "https://localhost:8080/delay/call/back");
        param.put("grey_version", "v5337");
        dscheduleRequest.setExtraParam(extraParam);

        DscheduleEventManage.pushDelayMessage(dscheduleRequest);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void httpWindowsDelayMessage() {
        Properties properties = System.getProperties();
        properties.put(EnvironmentConfig.HTTP_HOST, "https://localhost:8888");

        DscheduleRequest dscheduleRequest = new DscheduleRequest();
        dscheduleRequest.setDelayTime(5000);
        dscheduleRequest.setProtocolType(ProtocolType.HTTP.getProtocol());
        dscheduleRequest.setAppId("test");
        dscheduleRequest.setSeqId(String.valueOf(System.currentTimeMillis()));
        dscheduleRequest.setScene("default");

        Map<String, Object> param = new HashMap<>();
        param.put("other", "other");
        param.put("business_id", "123123213123123");
        dscheduleRequest.setParam(param);

        Map<String, Object> extraParam = new HashMap<>();
        param.put("call_back_url", "https://localhost:8080/delay/call/back");
        param.put("windows_package_delay_time", 5000);
        param.put("grey_version", "v5337");
        dscheduleRequest.setExtraParam(extraParam);

        DscheduleEventManage.pushDelayMessage(dscheduleRequest);
    }
}

