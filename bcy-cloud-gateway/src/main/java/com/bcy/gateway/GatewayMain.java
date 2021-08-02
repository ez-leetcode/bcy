package com.bcy.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import java.util.Objects;

@SpringBootApplication
//作为客户端注册进eureka
@EnableEurekaClient
public class GatewayMain {

    public static void main(String[] args) {
        SpringApplication.run(GatewayMain.class, args);
    }


    /*
    @Bean
    public KeyResolver ipKeyResolver(){
        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getHostName());
    }

      default-filters:
        - name: RequestRateLimiter #请求数限流 名字不能随便写
          args:
          key-resolver: "#{@ipKeyResolver}"  # 令牌解析器
          redis-rate-limiter.replenishRate: 1 #令牌桶每秒填充平均速率
          redis-rate-limiter.burstCapacity: 1 #令牌桶总容量；一般总容量为填充速率的2倍或3倍


     */
}
