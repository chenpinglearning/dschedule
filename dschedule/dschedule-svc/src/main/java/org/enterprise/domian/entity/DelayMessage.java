package org.enterprise.domian.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("delay_message")
public class DelayMessage {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String appId;
    private String scene;
    private Integer delayTime;
    private Integer dealStatus;
    private Integer retryTime;
    private Long retryIntervalTime;
    private String windowPackageTime;
    private Integer delayType;
    // unique index
    private String seqId;
    private String callBackUrl;
    private String greyVersion;
    private String businessParam;
    private String extraParam;
    private Long expireTime;
    private Date createTime;
    private Date updateTime;


    public static class DelayMessageFiled {
        public static String id = "id";
        public static String dealStatus = "deal_status";
        public static String delayTime = "delay_time";
        public static String retryIntervalTime = "retry_interval_time";
        public static String appId = "app_id";
        public static String scene = "scene";
        public static String seqId = "seq_id";
        public static String expireTime = "expire_time";
    }
}
