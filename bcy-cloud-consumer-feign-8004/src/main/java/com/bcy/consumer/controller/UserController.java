package com.bcy.consumer.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.consumer.service.UserFeignService;
import com.bcy.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserFeignService userFeignService;

    @GetMapping("/user/test")
    public Result<JSONObject> testGet(){
        return userFeignService.getTest();
    }

    @GetMapping("/user/timeout")
    public Result<JSONObject> globalTimeout(){
        return null;
    }

}
