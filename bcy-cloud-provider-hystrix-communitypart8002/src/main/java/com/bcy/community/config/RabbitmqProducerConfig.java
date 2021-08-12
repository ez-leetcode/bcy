package com.bcy.community.config;

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
    public DirectExchange askExchange(){
        //提问推送交换机
        log.info("正在创建提问推送交换机");
        return new DirectExchange(RabbitmqConfig.ASK_EXCHANGE_NAME);
    }

    //队列
    @Bean
    public Queue askQueue(){
        //提问推送队列
        log.info("正在创建提问推送队列");
        return new Queue(RabbitmqConfig.ASK_QUEUE_NAME,true);
    }

    //队列和交换机绑定
    @Bean
    public Binding bindingAskQueueExchange(){
        log.info("提问推送队列与交换机绑定成功");
        return BindingBuilder.bind(askQueue()).to(askExchange()).with(RabbitmqConfig.ASK_ROUTING_KEY);
    }


}
