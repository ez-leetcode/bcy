package com.bcy.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


//对需要鉴权的url，调用鉴权服务校验身份，通过则网关转发，否则直接返回。
@Configuration
@Slf4j
public class AccessGatewayFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthClient authClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.info(request.getMethod().name());
        log.info(request.toString());
        String url = request.getPath().value();
        log.info("url：" + url);
        if (authClient.hasPermissionControl(url)) {
            log.info("url指向非认证服务器，正在进行token检查");
            if (authClient.accessable(request)) {
                //通过
                log.info("token正确，转接服务");
                return chain.filter(exchange);
            }
            //没通过
            log.info("token不正确，正在返回身份验证出错提示");
            return unauthorized(exchange);
        }
        //没许可
        log.info("url指向认证服务器，正在提供转接");
        return chain.filter(exchange);
    }

    //身份验证出错
    private Mono<Void> unauthorized(ServerWebExchange serverWebExchange) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        DataBuffer buffer = serverWebExchange.getResponse()
                .bufferFactory().wrap(HttpStatus.UNAUTHORIZED.getReasonPhrase().getBytes());
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }

    @Override
    public int getOrder() {
        return 2;
    }


}