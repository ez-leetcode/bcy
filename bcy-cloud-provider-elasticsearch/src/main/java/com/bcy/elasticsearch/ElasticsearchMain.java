package com.bcy.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ElasticsearchMain {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchMain.class,args);
    }

}
