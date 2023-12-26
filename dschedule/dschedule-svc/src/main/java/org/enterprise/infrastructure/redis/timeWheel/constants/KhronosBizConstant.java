package org.enterprise.infrastructure.redis.timeWheel.constants;

/**
 *
 **/
public class KhronosBizConstant {

    /**
     * time = 100
     */
    public static final Integer TICK_DURATION = 100;
    /**
     * slot size = 36000
     */
    public static final Integer SIMPLE_SLOT_NUM = 36000;
    /**
     *
     */
    public static final String TIME_WHEEL_CURRENT_POINT = "time:wheel:current:point";
    /**
     *
     */
    public static final String TIME_WHEEL_SLOT = "time:wheel:slot:";
    /**
     * move point key
     */
    public static final String TURN_WHEEL_BLOCK_REDIS_KEY = "turn:wheel:block:redis:key";


    /**
     * slot size = 100
     */
    public static final Integer WINDOWS_PACKAGE_SIMPLE_SLOT_NUM = 100;


    /**
     *
     */
    public static final String SPLIT = ":";
}
