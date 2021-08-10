package com.bcy.websocket.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.config.RabbitmqConfig;
import com.bcy.mq.FansMsg;
import com.bcy.mq.HotCosMsg;
import com.bcy.mq.HotQAMsg;
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
    public void fansListenerQueue(Channel channel, Message message, FansMsg fansMsg) throws IOException {
        log.info("已接收到粉丝推送消息");
        log.info(message.toString());
        log.info(fansMsg.toString());
        //websocket处理
        websocketService.sendFansMessage(fansMsg);
        //手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("粉丝推送消息已被成功消费");
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitmqConfig.HOT_COS_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(value = RabbitmqConfig.HOT_COS_EXCHANGE_NAME),key = RabbitmqConfig.HOT_COS_ROUTING_KEY))
    @RabbitHandler
    public void hotCosListenerQueue(Channel channel, Message message, HotCosMsg hotCosMsg) throws IOException{
        log.info("已接收到热点cos推送消息");
        log.info(message.toString());
        log.info(hotCosMsg.toString());
        //websocket处理
        websocketService.sendHotCosToEveryOneOnline(hotCosMsg);
        //手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("推送热门cos成功");
    }



    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitmqConfig.HOT_COS_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(value = RabbitmqConfig.HOT_COS_EXCHANGE_NAME),key = RabbitmqConfig.HOT_COS_ROUTING_KEY))
    @RabbitHandler
    public void hotQAListenerQueue(Channel channel, Message message, HotQAMsg hotQAMsg)throws IOException{
        log.info("已接收到热点cos推送消息");
        log.info(message.toString());
        log.info(hotQAMsg.toString());
        //websocket处理
        websocketService.sendHotQAToEveryOneOnline(hotQAMsg);
        //手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("推送热门问题成功");
    }

}