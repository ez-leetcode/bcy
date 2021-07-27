package com.bcy.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@MapperScan("com.bcy.community.mapper")
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableCaching
public class CommunitypartHystrixMain {

    public static void main(String[] args) {
        SpringApplication.run(CommunitypartHystrixMain.class,args);
    }

}
