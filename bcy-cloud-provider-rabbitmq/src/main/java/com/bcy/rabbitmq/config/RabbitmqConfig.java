package com.bcy.rabbitmq.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitmqConfig {

    public static final String SMS_EXCHANGE_NAME = "sms_exchange";

    public static final String SMS_QUEUE_NAME = "sms_queue";

    public static final String SMS_ROUTING_KEY = "sms_routing_key";

    public static final String BOX_EXCHANGE_NAME = "box_exchange";

    public static final String BOX_QUEUE_NAME = "box_queue";

    public static final String BOX_ROUTING_KEY = "box_routing_key";

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.info("ConfirmCallback:     "+"相关数据："+correlationData);
            log.info("ConfirmCallback:     "+"确认情况："+ack);
            log.info("ConfirmCallback:     "+"原因："+cause);
        });
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info("ReturnCallback:     "+"消息："+message);
            log.info("ReturnCallback:     "+"回应码："+replyCode);
            log.info("ReturnCallback:     "+"回应信息："+replyText);
            log.info("ReturnCallback:     "+"交换机："+exchange);
            log.info("ReturnCallback:     "+"路由键："+routingKey);
        });
        return rabbitTemplate;
    }


}
