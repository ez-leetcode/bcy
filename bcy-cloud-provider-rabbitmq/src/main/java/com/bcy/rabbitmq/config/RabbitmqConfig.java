package com.bcy.rabbitmq.config;


import lombok.extern.slf4j.Slf4j;
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





}
