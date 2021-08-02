package com.bcy.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Base64;

//服务网关过滤器，结合spring security oauth2
//请求头中赋予网关服务合法客户端身份。对应鉴权服务AuthorizationServerConfig中gateway-client的用户名密码。


//@Configuration
//@Slf4j
//public class GateWayFilter implements GlobalFilter, Ordered {
/*
    private static final String GATEWAY_CLIENT_AUTHORIZATION = "Basic " +
            Base64.getEncoder().encodeToString("gateway-client:123456".getBytes());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("2");
        //网关身份
        ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
        builder.header("Authorization", GATEWAY_CLIENT_AUTHORIZATION);
        return chain.filter(exchange);
    }

    //过滤等级最高
    @Override
    public int getOrder() {
        log.info("1");
        return 1;
    }


 */
//}

