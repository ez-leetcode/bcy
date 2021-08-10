package com.bcy.quartz.service;

import com.bcy.config.RabbitmqConfig;
import com.bcy.mq.HotCosMsg;
import com.bcy.mq.HotQAMsg;
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

    public void sendHotCos(HotCosMsg hotCosMsg){
        log.info("正在向消息队列发送热门cos数据");
        log.info(hotCosMsg.toString());
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        //把消息放到对应路由中去
        rabbitTemplate.convertAndSend(RabbitmqConfig.HOT_COS_EXCHANGE_NAME,RabbitmqConfig.HOT_COS_ROUTING_KEY,hotCosMsg,correlationData);
    }

    public void sendHotQA(HotQAMsg hotQAMsg){
        log.info("正在向消息队列发送热门问答数据");
        log.info(hotQAMsg.toString());
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        //把消息放到对应路由中去
        rabbitTemplate.convertAndSend(RabbitmqConfig.HOT_QA_EXCHANGE_NAME,RabbitmqConfig.HOT_QA_ROUTING_KEY,hotQAMsg,correlationData);
    }

}
