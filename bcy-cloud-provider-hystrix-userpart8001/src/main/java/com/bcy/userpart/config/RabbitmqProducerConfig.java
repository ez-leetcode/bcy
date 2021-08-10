package com.bcy.userpart.config;

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
    public DirectExchange fansExchange(){
        //粉丝推送交换机
        log.info("正在创建粉丝推送交换机");
        return new DirectExchange(RabbitmqConfig.FANS_EXCHANGE_NAME);
    }

    //队列
    @Bean
    public Queue fansQueue(){
        //粉丝推送队列
        log.info("正在创建粉丝推送队列");
        return new Queue(RabbitmqConfig.FANS_QUEUE_NAME,true);
    }

    //队列和交换机绑定
    @Bean
    public Binding bindingFansQueueExchange(){
        log.info("粉丝推送队列与交换机绑定成功");
        return BindingBuilder.bind(fansQueue()).to(fansExchange()).with(RabbitmqConfig.FANS_ROUTING_KEY);
    }

}
