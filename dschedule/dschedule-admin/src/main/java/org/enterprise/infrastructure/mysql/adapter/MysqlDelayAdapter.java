package org.enterprise.infrastructure.mysql.adapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.enterprise.application.entrace.request.QueryDelayMessageRequest;
import org.enterprise.infrastructure.mysql.entity.DelayMessage;
import org.enterprise.infrastructure.mysql.mapper.DelayMessageMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
@Slf4j
@Component
public class MysqlDelayAdapter {
    @Resource
    private DelayMessageMapper delayMessageMapper;


    public Page<DelayMessage> queryDealMessagesPage(QueryDelayMessageRequest request) {
        QueryWrapper<DelayMessage> queryWrapper = new QueryWrapper<>();
        if (request.getAppId() != null) {
            queryWrapper.eq(DelayMessage.DelayMessageFiled.appId, request.getAppId());
        }

        if (request.getScene() != null) {
            queryWrapper.eq(DelayMessage.DelayMessageFiled.scene, request.getScene());
        }

        if (request.getSeqId() != null) {
            queryWrapper.eq(DelayMessage.DelayMessageFiled.seqId, request.getSeqId());
        }

        if (request.getStatus() != null) {
            queryWrapper.eq(DelayMessage.DelayMessageFiled.dealStatus, request.getStatus());
        }

        queryWrapper.orderBy(true, false, DelayMessage.DelayMessageFiled.createTime);

        Page<DelayMessage> page = new Page<>(request.getPage(), request.getSize());
        return delayMessageMapper.selectPage(page, queryWrapper);
    }


}
