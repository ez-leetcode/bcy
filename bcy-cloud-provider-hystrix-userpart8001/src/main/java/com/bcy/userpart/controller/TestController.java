package com.bcy.userpart.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;
import com.bcy.userpart.service.TestService;
import com.bcy.userpart.service.TimeoutService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private TimeoutService timeoutService;

    @GetMapping("/user/test")
    //表示启用hystrix服务熔断，服务发生超时或者错误会回调备用方法
    @HystrixCommand(fallbackMethod = "timeoutHandler",commandProperties = {
            //开启断路器
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),
            //时间范围内请求峰值，超过峰值触发熔断
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "100"),
            //时间范围大小
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),
            //失败率熔断限制
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60")
    })
    public Result<JSONObject> getTest(){
        return testService.getTest();
    }

    public Result<JSONObject> timeoutHandler(){
        log.error("服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

}
