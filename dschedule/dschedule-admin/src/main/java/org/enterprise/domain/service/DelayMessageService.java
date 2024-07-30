package org.enterprise.domain.service;

import org.enterprise.application.entrace.request.QueryDelayMessageRequest;
import org.enterprise.application.entrace.response.DelayMessageListResult;

public interface DelayMessageService {

    DelayMessageListResult queryDealMessagesLists(QueryDelayMessageRequest request);
}
