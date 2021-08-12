package com.bcy.community.service;

import com.bcy.config.RabbitmqConfig;
import com.bcy.mq.AskMsg;
import com.bcy.mq.AtMsg;
import com.bcy.mq.CommentMsg;
import com.bcy.mq.LikeMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RabbitmqProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //推送提问
    public void sendAskMessage(AskMsg askMsg){
        log.info("正在向消息队列中推送用户提问");
        log.info(askMsg.toString());
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitmqConfig.ASK_EXCHANGE_NAME,RabbitmqConfig.ASK_ROUTING_KEY,askMsg,correlationData);
    }

}
