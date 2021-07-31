package com.bcy.consumer.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.consumer.service.CommunityFeignService;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class CommunityController {

    @Autowired
    private CommunityFeignService communityFeignService;

    @GetMapping("/community/othersInfo")
    public Result<JSONObject> getOthersInfo(@RequestParam("id") Long id){
        return communityFeignService.getOthersInfo(id);
    }

    @PostMapping("/community/ask")
    public Result<JSONObject> deleteAsk(@RequestParam("id") Long id,@RequestParam("number") Long number){
        return communityFeignService.deleteAsk(id,number);
    }

    @DeleteMapping("/community/ask")
    public Result<JSONObject> addAsk(@RequestParam("fromId") Long fromId,@RequestParam("toId") Long toId,
                                     @RequestParam("question") String question){
        return communityFeignService.addAsk(fromId, toId, question);
    }

    @PostMapping("/community/answer")
    public Result<JSONObject> addAnswer(@RequestParam("number") Long number,@RequestParam("id") Long id,
                                        @RequestParam("answer") String answer){
        return communityFeignService.addAnswer(number, id, answer);
    }
}
