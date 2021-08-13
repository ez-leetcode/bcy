package com.bcy.websocket.service;

import com.bcy.config.RabbitmqConfig;
import com.bcy.mq.TalkAckMsg;
import com.bcy.mq.TalkMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RabbitmqProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //推送聊天消息
    public void sendTalkMsg(TalkMsg talkMsg){
        log.info("正在向消息队列推送聊天消息");
        log.info(talkMsg.toString());
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitmqConfig.WEBSOCKET_FANOUT_EXCHANGE_NAME,RabbitmqConfig.WEBSOCKET_FANOUT_ROUTING_KEY,talkMsg,correlationData);
    }

    //推送ack消息
    public void sendAckMsg(TalkAckMsg talkAckMsg){
        log.info("正在向消息队列中推送返回的ack");
        log.info(talkAckMsg.toString());
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitmqConfig.ACK_EXCHANGE_NAME,RabbitmqConfig.ACK_ROUTING_KEY,talkAckMsg,correlationData);
    }


}
