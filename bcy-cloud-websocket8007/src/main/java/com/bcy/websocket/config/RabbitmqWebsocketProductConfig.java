package com.bcy.websocket.config;

import com.bcy.config.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitmqWebsocketProductConfig {

    //创建广播队列
    @Bean
    public Queue broadcastQueue(){
        log.info("创建了广播队列");
        //持久化，确保消息不易丢失
        return new Queue(RabbitmqConfig.WEBSOCKET_FANOUT_QUEUE_NAME,true);
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        log.info("创建了广播交换机");
        return new FanoutExchange(RabbitmqConfig.WEBSOCKET_FANOUT_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(){
        log.info("正在绑定广播交换机与队列");
        return BindingBuilder.bind(broadcastQueue()).to(fanoutExchange());
    }

    @Bean
    public Queue ackQueue(){
        log.info("创建了ack广播队列");
        return new Queue(RabbitmqConfig.ACK_QUEUE_NAME,true);
    }

    @Bean
    public FanoutExchange ackExchange(){
        log.info("创建了ack交换机");
        return new FanoutExchange(RabbitmqConfig.ACK_EXCHANGE_NAME);
    }

    //这里路由键不知道
    @Bean
    public Binding ackBinding(){
        log.info("正在绑定ack广播交换机与队列");
        return BindingBuilder.bind(ackQueue()).to(ackExchange());
    }

}
