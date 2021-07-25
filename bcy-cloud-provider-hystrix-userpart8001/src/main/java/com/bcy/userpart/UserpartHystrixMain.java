package com.bcy.userpart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class UserpartHystrixMain {

    public static void main(String[] args) {
        SpringApplication.run(UserpartHystrixMain.class,args);
    }

}
