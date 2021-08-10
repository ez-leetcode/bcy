package com.bcy.websocket.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.config.RabbitmqConfig;
import com.bcy.mq.FansMsg;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class RabbitmqConsumerService {

    @Autowired
    private WebsocketService websocketService;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitmqConfig.FANS_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(value = RabbitmqConfig.FANS_EXCHANGE_NAME),key = RabbitmqConfig.FANS_ROUTING_KEY))
    @RabbitHandler
    public void ListenerQueue(Channel channel, Message message, FansMsg fansMsg) throws IOException {
        log.info("已接收到粉丝推送消息");
        log.info(message.toString());
        log.info(fansMsg.toString());
        //websocket处理
        websocketService.sendFansMessage(fansMsg);
        //手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("粉丝推送消息已被成功消费");
    }

}