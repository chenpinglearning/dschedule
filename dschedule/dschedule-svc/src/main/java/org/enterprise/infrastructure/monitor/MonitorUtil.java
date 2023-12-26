package org.enterprise.infrastructure.monitor;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;

/**
 * @author: albert.chen
 * @create: 2023-12-25
 * @description:
 */
public class MonitorUtil {
    private static Counter route_call_ops_error_total = Counter.build()
            .name("route_call_ops_error_total").labelNames("case")
            .help("调度服务调用ops获取订单车型异常次数").register();

    private static Counter router_get_order_vehicle_info_total = Counter.build()
            .name("router_get_order_vehicle_info_total")
            .labelNames("vehicle_type", "order_type", "business_type", "vehicle_name")
            .help("router从ops获取订单车型总数")
            .register();

    private static Counter ai_oas_order_create_all_total = Counter.build()
            .name("ai_oas_order_create_all_total")
            .labelNames("order_type", "business_type", "vehicle_attr", "vehicle_name")
            .help("订单创建总数").register();

    private static Counter ai_oas_order_create_city_all_total = Counter.build()
            .name("ai_oas_order_create_city_all_total")
            .labelNames("city_id")
            .help("城市唯独订单创建总数").register();

    private static Counter ai_router_order_pool_city_total = Counter.build()
            .name("ai_router_order_pool_city_total")
            .labelNames("city_id")
            .help("城市唯独订单池中订单个数监控分城市").register();

    private static Counter ai_router_order_pool_total = Counter.build()
            .name("ai_router_order_pool_total")
            .labelNames("case", "vehicle_attr", "order_type", "business_type", "vehicle_name")
            .help("唯独订单池中订单个数监控分城市").register();

    private static Counter ai_special_order = Counter.build()
            .name("ai_special_order")
            .labelNames("case", "vehicle_attr", "order_type", "business_type", "vehicle_name")
            .help("拉拉值监控").register();

    private static Gauge ai_router_delay_order_pool_all_total = Gauge.build()
            .name("ai_router_delay_order_pool_all_total")
            .labelNames("type", "case")
            .help("延时订单池中订单个数监控").register();


    private static Counter khronos_time_wheel_in_task = Counter.build()
            .name("khronos_time_wheel_in_task")
            .labelNames("action")
            .help("khronos_time_wheel_in_task").register();

    private static Counter khronos_turn_time_error_count = Counter.build()
            .name("khronos_turn_time_error_count")
            .labelNames("action")
            .help("khronos_turn_time_error_count").register();


    private static Gauge khronos_time_wheel_task_count = Gauge.build()
            .name("khronos_time_wheel_task_count")
            .labelNames("action")
            .help("khronos_time_wheel_task_count").register();

    private static Gauge khronos_time_wheel_delay_time = Gauge.build()
            .name("khronos_time_wheel_delay_time")
            .labelNames("action")
            .help("khronos_time_wheel_delay_time").register();

    private static Gauge khronos_turn_take_time_count = Gauge.build()
            .name("khronos_turn_take_time_count")
            .labelNames("action")
            .help("khronos_turn_take_time_count").register();

    private static Counter ai_order_push_rule_degradation_total = Counter.build()
            .name("ai_order_push_rule_degradation_total").labelNames("case")
            .help("自动播单降级告警").register();

    private static Counter ai_cancel_order_flag_total = Counter.build()
            .name("ai_cancel_order_flag_total").labelNames("bus")
            .help("消单开启告警").register();

    private static Counter ai_silence_order_total = Counter.build()
            .name("ai_silence_order_total").labelNames("business_type")
            .help("静默订单数量").register();

    private static Counter ai_op_wheeltime_error_total = Counter.build()
            .name("ai_op_wheeltime_error_total").labelNames("op_type")
            .help("延迟队列异常告警").register();

    private static Gauge ai_repush_order_num_total = Gauge.build()
            .name("ai_repush_order_num_total").labelNames("case")
            .help("48小时待无人响应重推订单数量").register();

    private static Counter ai_order_retry_to_geo_error_total = Counter.build()
            .name("ai_order_retry_to_geo_error_total").labelNames("case")
            .help("调用geo失败的核心场景异常").register();

    private static Counter ai_router_repush_all_total = Counter.build()
            .name("ai_router_repush_all_total")
            .labelNames("vehicle_attr", "order_type", "business_type", "vehicle_name")
            .help("无人响应重推统计监控").register();


    private static Counter ai_router_all_push_all_total = Counter.build()
            .name("ai_router_all_push_all_total")
            .labelNames("push_type", "vehicle_attr", "order_type", "business_type", "vehicle_name")
            .help("重推qps统计监控").register();


    private static Counter ai_router_repush_city_all_total = Counter.build()
            .name("ai_router_repush_city_all_total")
            .labelNames("city_id")
            .help("城市唯独无人响应重推统计监控").register();

    private static Counter ai_router_repush_in_type_total = Counter.build()
            .name("ai_router_repush_in_type_total").labelNames("vehicle_attr", "order_type", "business_type", "vehicle_name")
            .help("生成无人响应重推记录数量").register();

    private static Counter ai_router_get_pk_rule_exception_total = Counter.build()
            .name("ai_router_get_pk_rule_exception_total").labelNames("case", "order_type", "vehicle_name")
            .help("获取pk规则异常").register();


    private static Histogram ai_router_repush_interval_time = Histogram.build()
            .name("ai_router_repush_interval_time")
            .help("推送间隔时间")
            .exponentialBuckets(10, 1.5, 20)
            .labelNames("vehicle_attr", "order_type", "business_type", "vehicle_name")
            .register();

    private static Counter ai_wechat_hold_order_num = Counter.build()
            .name("ai_wechat_hold_order_num").labelNames("city_id", "vehicle_name")
            .help("微信捂单订单单量").register();

    private static Counter ai_wechat_hold_order_total = Counter.build()
            .name("ai_wechat_hold_order_total").labelNames("city_id", "vehicle_name")
            .help("微信捂单订单次数").register();

    /**
     * router调用ops获取订单车型异常次数
     *
     * @return
     */
    public static Counter getOpsOrderVehicleInfoErrorList() {
        return route_call_ops_error_total;
    }

    public static Counter getOrderVehicleInfoTotal() {
        return router_get_order_vehicle_info_total;
    }

    /**
     * 新增订单总数
     */
    public static Counter getOrderCreateTotal() {
        return ai_oas_order_create_all_total;
    }

    /**
     * 单独把城市分拆出来，防止指标超出最大限制
     *
     * @return
     */
    public static Counter getOrderCreateCityTotal() {
        return ai_oas_order_create_city_all_total;
    }

    /**
     * 开启了自动降级的功能
     */
    public static Counter getOrderPushRuleDegradation() {
        return ai_order_push_rule_degradation_total;
    }

    /**
     * 开启了消单的功能
     */
    public static Counter setCancelOrderFlagOrder() {
        return ai_cancel_order_flag_total;
    }

    /**
     * 订单池中订单个数监控分城市
     */
    public static Counter getOrderPoolCityTotal() {
        return ai_router_order_pool_city_total;
    }


    public static Counter getOrderPoolTotal() {
        return ai_router_order_pool_total;
    }

    /**
     * 拉拉值监控
     */
    public static Counter getAiSpecialOrder() {
        return ai_special_order;
    }

    /**
     * 延时订单池中订单的个数
     */
    public static Gauge getDelayOrderPoolTotal() {
        return ai_router_delay_order_pool_all_total;
    }

    /**
     * 进入槽位的qps
     *
     * @return
     */
    public static Counter getKhronosTimeWheelInTask() {
        return khronos_time_wheel_in_task;
    }

    /**
     * 移动指针失败统计
     *
     * @return
     */
    public static Counter getKhronosTurnTimeErrorCount() {
        return khronos_turn_time_error_count;
    }

    /**
     * 获取槽位的任务数量
     *
     * @return
     */
    public static Gauge getKhronosTimeWheelTaskCount() {
        return khronos_time_wheel_task_count;
    }

    /**
     * 任务延迟监控
     *
     * @return
     */
    public static Gauge getKhronosTimeWheelDelayTime() {
        return khronos_time_wheel_delay_time;
    }

    /**
     * 移动指针耗时监控
     *
     * @return
     */
    public static Gauge getKhronosTurnTakeTimeCount() {
        return khronos_turn_take_time_count;
    }

    /**
     * 无人响应重推统计监控
     */
    public static Counter getRepushAllTotal() {
        return ai_router_repush_all_total;
    }


    /**
     * 所有重推统计监控
     */
    public static Counter getAiRouterAllPushAllTotal() {
        return ai_router_all_push_all_total;
    }


    public static Counter getRepushCityAllTotal() {
        return ai_router_repush_city_all_total;
    }


    public static Counter getSilenceOrderTotal() {
        return ai_silence_order_total;
    }


    public static Counter getOpWheelTimrErrorTotal() {
        return ai_op_wheeltime_error_total;
    }


    public static Gauge getRepushOrderNumTotal() {
        return ai_repush_order_num_total;
    }

    public static Counter getOrderRetryToGeoErrorTotal() {
        return ai_order_retry_to_geo_error_total;
    }


    public static Counter getRepushInTypeTotal() {
        return ai_router_repush_in_type_total;
    }


    public static Histogram getRepushIntervalTimeTotal() {
        return ai_router_repush_interval_time;
    }

    public static Counter getPkRuleExceptionTotal() {
        return ai_router_get_pk_rule_exception_total;
    }

    public static Counter getAiWechatHoldOrderNum() {
        return ai_wechat_hold_order_num;
    }

    public static Counter getAiWechatHoldOrderTotal() {
        return ai_wechat_hold_order_total;
    }


}
