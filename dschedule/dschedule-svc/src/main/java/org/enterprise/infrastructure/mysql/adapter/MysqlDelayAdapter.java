package org.enterprise.infrastructure.mysql.adapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.enterprise.api.request.DscheduleRequest;
import org.enterprise.domian.constants.DealStatus;
import org.enterprise.domian.entity.DelayMessage;
import org.enterprise.infrastructure.ProductAbstractDelayQueue;
import org.enterprise.infrastructure.mysql.mapper.DelayMessageMapper;
import org.enterprise.util.JacksonUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
@Component
public class MysqlDelayAdapter extends ProductAbstractDelayQueue {
    @Resource
    private DelayMessageMapper delayMessageMapper;


    public void save(DscheduleRequest dscheduleRequest) {
        DelayMessage delayMessage = new DelayMessage();
        delayMessage.setAppId(dscheduleRequest.getAppId());
        delayMessage.setScene(dscheduleRequest.getScene());
        delayMessage.setDelayTime(dscheduleRequest.getDelayTime());
        delayMessage.setExpireTime(System.currentTimeMillis() + dscheduleRequest.getDelayTime() * 1000);
        delayMessage.setDealStatus(DealStatus.NOT_DEAL.getStatus());
        delayMessage.setDelayType(dscheduleRequest.getDelayType());
        delayMessage.setRetryTime(0);
        delayMessage.setSeqId(dscheduleRequest.getSeqId());
        delayMessage.setBusinessParam(JacksonUtil.obj2String(dscheduleRequest.getParam()));
        delayMessage.setExtraParam(JacksonUtil.obj2String(dscheduleRequest.getExtraParam()));
        delayMessage.setCreateTime(new Date());
        delayMessage.setUpdateTime(new Date());

        if (dscheduleRequest.getExtraParam() != null) {
            delayMessage.setWindowPackageTime((String) dscheduleRequest.getExtraParam().get("windows_package_delay_time"));
            delayMessage.setCallBackUrl((String) dscheduleRequest.getExtraParam().get("call_back_url"));
            delayMessage.setGreyVersion((String) dscheduleRequest.getExtraParam().get("grey_version"));
        }

        delayMessageMapper.insert(delayMessage);
    }


    public List<DelayMessage> queryNotDealDelayMessages(int size) {
        QueryWrapper<DelayMessage> queryWrapper = new QueryWrapper<>();

        queryWrapper.le(Boolean.TRUE, DelayMessage.DelayMessageFiled.expireTime, System.currentTimeMillis());
        queryWrapper.eq(DelayMessage.DelayMessageFiled.dealStatus, DealStatus.NOT_DEAL.getStatus());
        Page<DelayMessage> page = delayMessageMapper.selectPage(Page.of(1, size), queryWrapper);
        List<DelayMessage> records = page.getRecords();
        List<Long> ids = records.stream().map(DelayMessage::getId).collect(Collectors.toList());

        //update dealIng
        QueryWrapper<DelayMessage> updateWrapper = new QueryWrapper<>();
        updateWrapper.in(DelayMessage.DelayMessageFiled.id, ids);
        DelayMessage delayMessage = new DelayMessage();
        delayMessage.setDealStatus(DealStatus.DEAL_ING.getStatus());
        delayMessageMapper.update(delayMessage, updateWrapper);

        return records;
    }


    public List<DelayMessage> queryDealFailDelayMessages(int size) {
        QueryWrapper<DelayMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DelayMessage.DelayMessageFiled.dealStatus, DealStatus.DEAL_FAIL.getStatus());

        Page<DelayMessage> page = delayMessageMapper.selectPage(Page.of(1, size), queryWrapper);
        return page.getRecords();
    }


    public void updateFailDelayMessage(String seqId) {
        QueryWrapper<DelayMessage> updateWrapper = new QueryWrapper<>();
        updateWrapper.in(DelayMessage.DelayMessageFiled.seqId, seqId);
        DelayMessage delayMessage = new DelayMessage();
        delayMessage.setDealStatus(DealStatus.DEAL_FAIL.getStatus());

        delayMessageMapper.update(delayMessage, updateWrapper);
    }


}
