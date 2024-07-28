package org.enterprise.infrastructure.redis.timeWheel.config;

import org.enterprise.util.JacksonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

@Configuration
public class TimeWheelConfig {

    @Value("${time.wheel.slot.num:36000}")
    private Integer simpleSlotNum;


    @Value("${time.wheel.delay.consume.hold:300}")
    private Integer delayConsume;

    /**
     * {
     *     "default": 100,
     *     "package": 20000
     * }
     */
    private Map<String, Integer> scenes;

    @Value("${time.wheel.scene:{\"default\":20000}}")
    public void setScenes(String scenes) {
        this.scenes = JacksonUtil.string2Obj(scenes , Map.class);
    }

    public Integer getSimpleSlotNum() {
        return simpleSlotNum;
    }

    public Integer getDelayConsume() {
        return delayConsume;
    }

    public Map<String, Integer> getScenes() {
        return scenes;
    }
}
