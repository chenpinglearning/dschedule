package org.enterprise.infrastructure.mysql.adapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.constants.Constant;
import org.enterprise.domian.constants.DealStatus;
import org.enterprise.domian.entity.DelayMessage;
import org.enterprise.infrastructure.ProductAbstractDelayQueue;
import org.enterprise.infrastructure.mysql.mapper.DelayMessageMapper;
import org.enterprise.util.JacksonUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
@Slf4j
@Component
public class MysqlDelayAdapter extends ProductAbstractDelayQueue {
    @Resource
    private DelayMessageMapper delayMessageMapper;


    public boolean save(DscheduleRequest dscheduleRequest) {
        DelayMessage delayMessage = new DelayMessage();
        delayMessage.setAppId(dscheduleRequest.getAppId());
        delayMessage.setScene(dscheduleRequest.getScene());
        delayMessage.setDelayTime(dscheduleRequest.getDelayTime());
        delayMessage.setExpireTime(System.currentTimeMillis() + dscheduleRequest.getDelayTime());
        delayMessage.setDealStatus(DealStatus.NOT_DEAL.getStatus());
        delayMessage.setDelayType(dscheduleRequest.getDelayType());
        delayMessage.setRetryTime(0);
        delayMessage.setSeqId(dscheduleRequest.getSeqId());
        delayMessage.setBusinessParam(JacksonUtil.obj2String(dscheduleRequest.getParam()));
        delayMessage.setExtraParam(JacksonUtil.obj2String(dscheduleRequest.getExtraParam()));
        delayMessage.setRetryIntervalTime(delayMessage.getExpireTime());
        delayMessage.setCreateTime(new Date());
        delayMessage.setUpdateTime(new Date());

        if (dscheduleRequest.getExtraParam() != null) {
            delayMessage.setWindowPackageTime((String) dscheduleRequest.getExtraParam().get(Constant.WindowsPackageTime));
            delayMessage.setCallBackUrl((String) dscheduleRequest.getExtraParam().get(Constant.CallBackUrl));
            delayMessage.setGreyVersion((String) dscheduleRequest.getExtraParam().get(Constant.GreyVersion));
        }

        try {
            delayMessageMapper.insert(delayMessage);
        } catch (DuplicateKeyException e) {
            log.error("seqId retry，please check {}", dscheduleRequest.getSeqId());
            return false;
        } catch (Exception e) {
            throw e;
        }

        return true;
    }



    public List<DelayMessage> queryDealMoreTwentySecondDelayMessages(int size) {
        QueryWrapper<DelayMessage> queryWrapper = new QueryWrapper<>();

        queryWrapper.le(Boolean.TRUE, DelayMessage.DelayMessageFiled.retryIntervalTime, System.currentTimeMillis() + 20000);
        queryWrapper.eq(DelayMessage.DelayMessageFiled.dealStatus, DealStatus.NOT_DEAL.getStatus());

        Page<DelayMessage> page = delayMessageMapper.selectPage(Page.of(1, size), queryWrapper);
        return page.getRecords();
    }

    public List<DelayMessage> queryDealFailDelayMessages(int size) {
        QueryWrapper<DelayMessage> queryWrapper = new QueryWrapper<>();

        queryWrapper.le(Boolean.TRUE, DelayMessage.DelayMessageFiled.retryIntervalTime, System.currentTimeMillis());
        queryWrapper.eq(DelayMessage.DelayMessageFiled.dealStatus, DealStatus.DEAL_FAIL.getStatus());
        queryWrapper.le(Boolean.TRUE, DelayMessage.DelayMessageFiled.retryTime, 3);

        Page<DelayMessage> page = delayMessageMapper.selectPage(Page.of(1, size), queryWrapper);
        return page.getRecords();
    }


    public void updateFailDelayMessage(String seqId) {
        DelayMessage delayMessage = queryDelayMessage(seqId);
        if (delayMessage == null) {
            log.error("delay message not found {}", seqId);
            return;
        }

        delayMessage.setRetryTime(delayMessage.getRetryTime() + 1);
        delayMessage.setDealStatus(DealStatus.DEAL_FAIL.getStatus());
        delayMessage.setRetryIntervalTime(System.currentTimeMillis() + Constant.retryIntervalTimes.get(delayMessage.getRetryTime() - 1));
        delayMessage.setUpdateTime(new Date());
        delayMessageMapper.updateById(delayMessage);
    }


    public DelayMessage queryDelayMessage(String seqId) {
        QueryWrapper<DelayMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DelayMessage.DelayMessageFiled.seqId, seqId);
        return delayMessageMapper.selectOne(queryWrapper);
    }

    public void updateSuccessDelayMessage(String seqId) {
        QueryWrapper<DelayMessage> updateWrapper = new QueryWrapper<>();
        updateWrapper.in(DelayMessage.DelayMessageFiled.seqId, seqId);
        DelayMessage delayMessage = new DelayMessage();
        delayMessage.setDealStatus(DealStatus.DEAL_SUCCESS.getStatus());

        delayMessageMapper.update(delayMessage, updateWrapper);
    }


}
