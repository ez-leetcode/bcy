package com.bcy.elasticsearch.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.elasticsearch.service.CosService;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CosController {


    @Autowired
    private CosService cosService;


    @RequestMapping("/es/test")
    public Result<JSONObject> test(@RequestParam("index") String index){
        return ResultUtils.getResult(new JSONObject(), cosService.test(index));
    }



}
