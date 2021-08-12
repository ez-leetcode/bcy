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

    //交换机
    @Bean
    public DirectExchange atExchange(){
        //@推送交换机
        log.info("正在创建@推送交换机");
        return new DirectExchange(RabbitmqConfig.AT_EXCHANGE_NAME);
    }

    //队列
    @Bean
    public Queue atQueue(){
        //@推送队列
        log.info("正在创建@推送队列");
        return new Queue(RabbitmqConfig.AT_QUEUE_NAME,true);
    }

    //队列和交换机绑定
    @Bean
    public Binding bindingAtQueueExchange(){
        log.info("@队列和交换机绑定成功");
        return BindingBuilder.bind(atQueue()).to(atExchange()).with(RabbitmqConfig.AT_ROUTING_KEY);
    }

    //交换机
    @Bean
    public DirectExchange likeExchange(){
        log.info("正在创建点赞交换机");
        return new DirectExchange(RabbitmqConfig.LIKE_EXCHANGE_NAME);
    }

    //队列
    @Bean
    public Queue likeQueue(){
        log.info("正在创建点赞队列");
        return new Queue(RabbitmqConfig.LIKE_QUEUE_NAME,true);
    }

    //队列和交换机绑定
    @Bean
    public Binding bindingLikeQueueExchange(){
        log.info("绑定点赞队列和交换机成功");
        return BindingBuilder.bind(likeQueue()).to(likeExchange()).with(RabbitmqConfig.LIKE_ROUTING_NAME);
    }

    //交换机
    @Bean
    public DirectExchange commentExchange(){
        log.info("正在创建评论交换机");
        return new DirectExchange(RabbitmqConfig.COMMENT_EXCHANGE_NAME);
    }

    //队列
    @Bean
    public Queue commentQueue(){
        log.info("正在创建评论队列");
        return new Queue(RabbitmqConfig.COMMENT_QUEUE_NAME,true);
    }

    //队列和交换机绑定
    @Bean
    public Binding bindingCommentQueueExchange(){
        log.info("绑定评论队列和交换机成功");
        return BindingBuilder.bind(commentQueue()).to(commentExchange()).with(RabbitmqConfig.COMMENT_ROUTING_KEY);
    }

}
