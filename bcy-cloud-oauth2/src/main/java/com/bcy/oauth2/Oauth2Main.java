package com.bcy.oauth2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bcy.oauth2.mapper")
public class Oauth2Main {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2Main.class,args);
    }

}
