package com.bcy.userpart.service;

import com.bcy.config.RabbitmqConfig;
import com.bcy.mq.FansMsg;
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

    //推送添加粉丝
    public void sendFansMsg(FansMsg fansMsg){
        log.info("正在向消息队列中加入推送添加粉丝信息");
        log.info(fansMsg.toString());
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitmqConfig.FANS_EXCHANGE_NAME,RabbitmqConfig.FANS_ROUTING_KEY,fansMsg,correlationData);
    }

}
