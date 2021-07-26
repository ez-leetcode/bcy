package com.bcy.consumer.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

//根据eureka里的微服务客户端信息转交给对应微服务提供者
//feign自带负载均衡，会在微服务提供者之间进行轮询或其他负载均衡办法
@Component
@FeignClient(value = "BCY-CLOUD-HYSTRIX-USERPART")
public interface UserFeignService {

    @GetMapping("/user/test")
    Result<JSONObject> getTest();

}