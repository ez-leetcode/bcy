package com.bcy.acgpart.service;

import com.bcy.config.RabbitmqConfig;
import com.bcy.mq.AtMsg;
import com.bcy.mq.CommentMsg;
import com.bcy.mq.EsMsg;
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

    //推送点赞
    public void sendLikeMessage(LikeMsg likeMsg){
        log.info("正在向消息队列中推送用户点赞");
        log.info(likeMsg.toString());
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitmqConfig.LIKE_EXCHANGE_NAME,RabbitmqConfig.LIKE_ROUTING_NAME,likeMsg,correlationData);
    }

    //推送@
    public void sendAtMessage(AtMsg atMsg){
        log.info("正在向消息队列中推送用户@");
        log.info(atMsg.toString());
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitmqConfig.AT_EXCHANGE_NAME,RabbitmqConfig.AT_ROUTING_KEY,atMsg,correlationData);
    }

    //推送评论
    public void sendCommentMessage(CommentMsg commentMsg){
        log.info("正在向消息队列中推送用户评论");
        log.info(commentMsg.toString());
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitmqConfig.COMMENT_EXCHANGE_NAME,RabbitmqConfig.COMMENT_ROUTING_KEY,commentMsg,correlationData);
    }

    public void sendEsMessage(EsMsg esMsg){
        log.info("正在向消息队列中推送es更新");
        log.info(esMsg.toString());
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitmqConfig.ES_EXCHANGE_NAME,RabbitmqConfig.ES_ROUTING_KEY,esMsg,correlationData);
    }

}
