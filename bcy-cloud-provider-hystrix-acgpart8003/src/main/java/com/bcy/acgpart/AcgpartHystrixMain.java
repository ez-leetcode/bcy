package com.bcy.acgpart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@MapperScan("com.bcy.acgpart.mapper")
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
public class AcgpartHystrixMain {

    public static void main(String[] args) {
        SpringApplication.run(AcgpartHystrixMain.class,args);
    }

}
