package com.bcy.quartz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableCaching
@EnableEurekaClient
@MapperScan("com.bcy.quartz.mapper")
public class QuartzMain {

    public static void main(String[] args) {
        SpringApplication.run(QuartzMain.class,args);
    }

}
