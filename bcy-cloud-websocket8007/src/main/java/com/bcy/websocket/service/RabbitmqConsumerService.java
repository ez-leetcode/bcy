package com.bcy.websocket.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.config.RabbitmqConfig;
import com.bcy.mq.*;
import com.bcy.websocket.mapper.TalkMessageMapper;
import com.bcy.websocket.mapper.TalkUserMapper;
import com.bcy.websocket.mapper.UserSettingMapper;
import com.bcy.websocket.pojo.UserSetting;
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

    //黑名单待完成
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitmqConfig.FANS_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(value = RabbitmqConfig.FANS_EXCHANGE_NAME),key = RabbitmqConfig.FANS_ROUTING_KEY))
    @RabbitHandler
    public void getTalk(Channel channel,Message message,TalkMsg talkMsg) throws IOException{
        log.info("已接收到用户消息");
        log.info(message.toString());
        log.info(talkMsg.toString());
        //待完成
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("接收用户数据成功");
    }


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
        log.info("已接收到热点问答推送消息");
        log.info(message.toString());
        log.info(hotQAMsg.toString());
        //websocket处理
        websocketService.sendHotQAToEveryOneOnline(hotQAMsg);
        //手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("推送热门问题成功");
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitmqConfig.ASK_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(value = RabbitmqConfig.ASK_EXCHANGE_NAME),key = RabbitmqConfig.ASK_ROUTING_KEY))
    @RabbitHandler
    public void sendAskListenerQueue(Channel channel, Message message, AskMsg askMsg)throws IOException{
        log.info("已接收到提问推送消息");
        log.info(message.toString());
        log.info(askMsg.toString());
        //websocket处理
        websocketService.sendAskMsg(askMsg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("推送用户提问成功");
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitmqConfig.AT_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(value = RabbitmqConfig.AT_EXCHANGE_NAME),key = RabbitmqConfig.AT_ROUTING_KEY))
    @RabbitHandler
    public void atListenerQueue(Channel channel, Message message, AtMsg atMsg)throws IOException{
        log.info("已收到@推送消息");
        log.info(message.toString());
        log.info(atMsg.toString());
        //websocket处理
        websocketService.sendAtMessage(atMsg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("用户@推送成功");
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitmqConfig.LIKE_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(value = RabbitmqConfig.LIKE_EXCHANGE_NAME),key = RabbitmqConfig.LIKE_ROUTING_NAME))
    @RabbitHandler
    public void likeListenerQueue(Channel channel,Message message,LikeMsg likeMsg)throws IOException{
        log.info("已收到点赞推送消息");
        log.info(message.toString());
        log.info(likeMsg.toString());
        //websocket处理
        websocketService.sendLikeMessage(likeMsg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("点赞消息推送成功");
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitmqConfig.COMMENT_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(value = RabbitmqConfig.COMMENT_EXCHANGE_NAME),key = RabbitmqConfig.COMMENT_ROUTING_KEY))
    @RabbitHandler
    public void commentListenerQueue(Channel channel,Message message,CommentMsg commentMsg)throws IOException{
        log.info("已收到评论推送消息");
        log.info(message.toString());
        log.info(commentMsg.toString());
        //websocket处理
        websocketService.sendCommentMessage(commentMsg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("评论消息推送成功");
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitmqConfig.ACK_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(value = RabbitmqConfig.ACK_EXCHANGE_NAME),key = RabbitmqConfig.ACK_ROUTING_KEY))
    @RabbitHandler
    public void ackListenerQueue(Channel channel,Message message,TalkAckMsg talkAckMsg) throws IOException{
        log.info("已收到ack返回消息");
        log.info(message.toString());
        log.info(talkAckMsg.toString());
        //websocket处理
        websocketService.sendAckMessage(talkAckMsg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("ack消息推送成功");
    }

}