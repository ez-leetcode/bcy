package com.bcy.userpart.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;
import com.bcy.userpart.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/user/test")
    public Result<JSONObject> getTest(){
        return testService.getTest();
    }

}
