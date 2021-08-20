package com.bcy.acgpart.config;

import com.bcy.config.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitmqProducerConfig {

    //交换机
    @Bean
    public DirectExchange esExchange(){
        log.info("正在创建es数据同步交换机");
        return new DirectExchange(com.bcy.config.RabbitmqConfig.ES_EXCHANGE_NAME);
    }

    //队列
    @Bean
    public Queue esQueue(){
        log.info("正在创建es数据同步队列");
        return new Queue(com.bcy.config.RabbitmqConfig.ES_QUEUE_NAME,true);
    }

    //队列和交换机绑定
    @Bean
    public Binding bindingEsQueueExchange(){
        log.info("es数据同步队列与交换机绑定成功");
        return BindingBuilder.bind(esQueue()).to(esExchange()).with(RabbitmqConfig.ES_ROUTING_KEY);
    }

    @Bean
    public DirectExchange esCircleExchange(){
        log.info("正在创建es圈子数据同步交换机");
        return new DirectExchange(RabbitmqConfig.ES_CIRCLE_EXCHANGE_NAME);
    }

    @Bean
    public Queue esCircleQueue(){
        log.info("正在创建es圈子数据同步队列");
        return new Queue(RabbitmqConfig.ES_CIRCLE_QUEUE_NAME,true);
    }

    @Bean
    public Binding bindingEsCircleQueueExchange(){
        log.info("es圈子数据同步队列与交换机绑定成功");
        return BindingBuilder.bind(esCircleQueue()).to(esCircleExchange()).with(RabbitmqConfig.ES_CIRCLE_ROUTING_KEY);
    }

}