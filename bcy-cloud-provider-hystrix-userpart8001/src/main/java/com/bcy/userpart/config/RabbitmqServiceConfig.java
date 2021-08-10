package com.bcy.userpart.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/*
@Component
@Slf4j
public class RabbitmqServiceConfig implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("消息已确认 cause:{} - {}",correlationData);
        } else {
            log.info("消息未确认 cause:{} - {}",correlationData);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息被退回 {}", message.toString());
    }
}


 */