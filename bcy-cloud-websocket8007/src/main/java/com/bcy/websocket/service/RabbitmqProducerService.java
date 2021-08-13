package com.bcy.websocket.service;

import com.bcy.config.RabbitmqConfig;
import com.bcy.mq.TalkAckMsg;
import com.bcy.mq.TalkMsg;
import com.bcy.websocket.utils.RedisUtils;
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

    @Autowired
    private RedisUtils redisUtils;

    //推送聊天消息
    public void sendTalkMsg(TalkMsg talkMsg){
        //把uuid放进redis，在下游接收的时候验证uuid是否重复，确保消息的幂等性
        //就设置1分钟好了
        redisUtils.saveByMinutesTime(talkMsg.getUuId(),talkMsg.getMsg(),1);
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