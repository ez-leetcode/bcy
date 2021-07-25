package com.bcy.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
//作为客户端注册进eureka
@EnableEurekaClient
public class GatewayMain {

    public static void main(String[] args) {
        SpringApplication.run(GatewayMain.class, args);
    }

}
