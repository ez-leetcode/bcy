package com.bcy.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ConsumerMain {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerMain.class,args);
    }

}
