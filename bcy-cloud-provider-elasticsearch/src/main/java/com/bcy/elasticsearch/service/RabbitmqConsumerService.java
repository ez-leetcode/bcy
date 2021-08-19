package com.bcy.elasticsearch.service;

import com.bcy.config.RabbitmqConfig;
import com.bcy.mq.EsMsg;
import com.bcy.mq.TalkMsg;
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
    private SyncService syncService;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitmqConfig.ES_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(value = RabbitmqConfig.ES_EXCHANGE_NAME),key = RabbitmqConfig.ES_ROUTING_KEY))
    @RabbitHandler
    public void getTalk(Channel channel, Message message, EsMsg esMsg) throws IOException {
        log.info("已接收到异步更新消息");
        log.info(message.toString());
        log.info(esMsg.toString());
        //待完成
        if(esMsg.getType() == 1){
            syncService.update(esMsg.getNumber(),1);
        }else if(esMsg.getType() == 2){
            syncService.delete(esMsg.getNumber(),1);
        }else if(esMsg.getType() == 3){
            syncService.update(esMsg.getNumber(),2);
        }else{
            syncService.delete(esMsg.getNumber(),2);
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        log.info("接收用户数据成功");
    }

}
