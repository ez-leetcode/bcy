package com.bcy.elasticsearch.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.elasticsearch.service.CosService;
import com.bcy.elasticsearch.service.SyncService;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CosController {


    @Autowired
    private CosService cosService;

    @Autowired
    private SyncService syncService;

    @PostMapping("/es/test")
    public Result<JSONObject> test(@RequestParam("number") Long number)throws IOException {
        syncService.update(number,1);
        return ResultUtils.getResult(new JSONObject(), "success");
    }

    @PostMapping("/es/test1")
    public Result<JSONObject> test1(@RequestParam("keyword") String keyword,@RequestParam("cnt") Integer cnt,
                                    @RequestParam("page") Integer page)throws IOException{
        cosService.searchCos(keyword, cnt, page);
        return ResultUtils.getResult(new JSONObject(),"success");
    }


}
