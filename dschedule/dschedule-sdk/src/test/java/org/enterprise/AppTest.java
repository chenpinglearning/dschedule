package org.enterprise;

import org.enterprise.api.DscheduleEventManage;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.constants.DscheduleType;
import org.enterprise.constants.EnvironmentConfig;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;


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
        properties.put(EnvironmentConfig.HTTP_HOST, "http://localhost:8888");

        DscheduleRequest dscheduleRequest = new DscheduleRequest();
        dscheduleRequest.setDelayTime(10 * 1000);
        dscheduleRequest.setDelayType(DscheduleType.RABBITMQ.getType());
        dscheduleRequest.setAppId("dschedule");
        dscheduleRequest.setSeqId(UUID.randomUUID().toString());
        dscheduleRequest.setScene("delayMessage");

        Map<String, Object> param = new HashMap<>();
        param.put("other", "other");
        param.put("business_id", "can search delay businessId");
        dscheduleRequest.setParam(param);

        Map<String, Object> extraParam = new HashMap<>();
        extraParam.put("call_back_url", "https://localhost:8080/delay/call/back");
        extraParam.put("grey_version", "v5337");
        dscheduleRequest.setExtraParam(extraParam);

        DscheduleEventManage.pushDelayMessage(dscheduleRequest);
    }


    @Test
    public void kafkaNormalDelayMessage() {
        Properties properties = System.getProperties();
        properties.put(EnvironmentConfig.HTTP_HOST, "http://localhost:8888");

        DscheduleRequest dscheduleRequest = new DscheduleRequest();
        dscheduleRequest.setDelayTime(10 * 1000);
        dscheduleRequest.setDelayType(DscheduleType.RABBITMQ.getType());
        dscheduleRequest.setAppId("dschedule");
        dscheduleRequest.setSeqId(UUID.randomUUID().toString());
        dscheduleRequest.setScene("delayMessage");

        Map<String, Object> param = new HashMap<>();
        param.put("other", "other");
        param.put("business_id", "can search delay businessId");
        dscheduleRequest.setParam(param);

        Map<String, Object> extraParam = new HashMap<>();
        extraParam.put("call_back_url", "kafka");
        extraParam.put("grey_version", "v5337");
        dscheduleRequest.setExtraParam(extraParam);

        DscheduleEventManage.pushDelayMessage(dscheduleRequest);
    }


    @Test
    public void httpWindowsDelayMessage() {
        Properties properties = System.getProperties();
        properties.put(EnvironmentConfig.HTTP_HOST, "http://localhost:8888");

        DscheduleRequest dscheduleRequest = new DscheduleRequest();
        dscheduleRequest.setDelayTime(40000);
        dscheduleRequest.setDelayType(DscheduleType.REDIS.getType());
        dscheduleRequest.setAppId("dschedule");
        dscheduleRequest.setSeqId(UUID.randomUUID().toString());
        dscheduleRequest.setScene("default");

        Map<String, Object> param = new HashMap<>();
        param.put("other", "other");
        param.put("business_id", "can search delay businessId");
        dscheduleRequest.setParam(param);

        Map<String, Object> extraParam = new HashMap<>();
        extraParam.put("call_back_url", "https://localhost:8080/delay/call/back");
        extraParam.put("windows_package", 10000);
        extraParam.put("grey_version", "v5337");
        dscheduleRequest.setExtraParam(extraParam);

        DscheduleEventManage.pushDelayMessage(dscheduleRequest);
    }


    @Test
    public void kafkaWindowsDelayMessage() {
        Properties properties = System.getProperties();
        properties.put(EnvironmentConfig.HTTP_HOST, "http://localhost:8888");

        DscheduleRequest dscheduleRequest = new DscheduleRequest();
        dscheduleRequest.setDelayTime(40000);
        dscheduleRequest.setDelayType(DscheduleType.REDIS.getType());
        dscheduleRequest.setAppId("dschedule");
        dscheduleRequest.setSeqId(UUID.randomUUID().toString());
        dscheduleRequest.setScene("default");

        Map<String, Object> param = new HashMap<>();
        param.put("other", "other");
        param.put("business_id", "can search delay businessId");
        dscheduleRequest.setParam(param);

        Map<String, Object> extraParam = new HashMap<>();
        extraParam.put("call_back_url", "kafka");
        extraParam.put("windows_package", 10000);
        extraParam.put("grey_version", "v5337");
        dscheduleRequest.setExtraParam(extraParam);

        DscheduleEventManage.pushDelayMessage(dscheduleRequest);
    }
}

