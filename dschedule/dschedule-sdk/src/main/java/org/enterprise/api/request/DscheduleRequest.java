package org.enterprise.api.request;

import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: albert.chen
 * @create: 2023-11-28
 * @description:
 */
public class DscheduleRequest {
    /**
     * timestamp(delay expire time)
     */
    @NotNull
    private long delayTime;
    /**
     * DscheduleType -- enum DscheduleType
     */
    private Integer delayType;
    /**
     * protocolType -- enum ProtocolType
     */
    private Integer protocolType;
    /**
     * applicationId
     */
    @NotNull
    private String appid;
    /**
     * requestId
     */
    @NotNull
    private String seqId;
    /**
     * delay business scene
     */
    private String scene = "default";
    /**
     * business param(callback、view)
     */
    private Map<String, Object> param = new HashMap<>();
    /**
     * for example
     * call_back_url = "https://host/call_back_url"
     * grey_version = v5337
     * windows_package = 5000ms
     */
    private Map<String, Object> extraParam = new HashMap<>();


    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public Integer getDelayType() {
        return delayType;
    }

    public void setDelayType(Integer delayType) {
        this.delayType = delayType;
    }

    public Integer getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(Integer protocolType) {
        this.protocolType = protocolType;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSeqId() {
        return seqId;
    }

    public void setSeqId(String seqId) {
        this.seqId = seqId;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public Map<String, Object> getExtraParam() {
        return extraParam;
    }

    public void setExtraParam(Map<String, Object> extraParam) {
        this.extraParam = extraParam;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }


    @Override
    public String toString() {
        return "DscheduleRequest{" +
                "delayTime=" + delayTime +
                ", delayType=" + delayType +
                ", protocolType=" + protocolType +
                ", appid='" + appid + '\'' +
                ", seqId='" + seqId + '\'' +
                ", scene='" + scene + '\'' +
                ", param=" + param +
                ", extraParam=" + extraParam +
                '}';
    }
}
