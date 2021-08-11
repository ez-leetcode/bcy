package com.bcy.consumer.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.consumer.service.CommunityFeignService;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

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

    @GetMapping("/community/atList")
    public Result<JSONObject> getAtMessage(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                           @RequestParam("page") Long page){
        return communityFeignService.getAtMessage(id, cnt, page);
    }

    @GetMapping("/community/likeList")
    public Result<JSONObject> getLikeMessage(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                             @RequestParam("page") Long page){
        return communityFeignService.getLikeMessage(id, cnt, page);
    }

    @GetMapping("/community/commentList")
    public Result<JSONObject> getCommentList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                             @RequestParam("page") Long page){
        return communityFeignService.getCommentList(id, cnt, page);
    }

    @GetMapping("/community/allCounts")
    public Result<JSONObject> getAllCounts(@RequestParam("id") Long id){
        return communityFeignService.getAllCounts(id);
    }

    @GetMapping("/community/askList")
    public Result<JSONObject> getAskList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                         @RequestParam("page") Long page){
        return communityFeignService.getAskList(id, cnt, page);
    }

    @GetMapping("/community/judgeOnline")
    public Result<JSONObject> judgeOnline(@RequestParam("userId") List<Long> userId){
        return communityFeignService.judgeOnline(userId);
    }

    @DeleteMapping("/community/talkHistory")
    public Result<JSONObject> deleteTalkHistory(@RequestParam("id") Long id,
                                                @RequestParam("number") List<Long> number){
        return communityFeignService.deleteTalkHistory(id, number);
    }

    @DeleteMapping("/community/talk")
    public Result<JSONObject> deleteTalk(@RequestParam("id") Long id,@RequestParam("toId") Long toId){
        return communityFeignService.deleteTalk(id, toId);
    }

    @GetMapping("/community/talkCounts")
    public Result<JSONObject> getTalkCounts(@RequestParam("id") Long id,@RequestParam("toId") List<Long> toId) throws ParseException {
        return communityFeignService.getTalkCounts(id, toId);
    }

    @GetMapping("/community/talkList")
    public Result<JSONObject> getTalkList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                          @RequestParam("page") Long page){
        return communityFeignService.getTalkList(id, cnt, page);
    }

    @PostMapping("/community/allRead")
    public Result<JSONObject> allRead(@RequestParam("id") Long id,@RequestParam("toId") Long toId){
        return communityFeignService.allRead(id, toId);
    }
}
