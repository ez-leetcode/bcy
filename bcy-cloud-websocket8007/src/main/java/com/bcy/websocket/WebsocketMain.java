package com.bcy.websocket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@MapperScan("com.bcy.websocket.mapper")
@SpringBootApplication
public class WebsocketMain {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketMain.class,args);
    }

}
