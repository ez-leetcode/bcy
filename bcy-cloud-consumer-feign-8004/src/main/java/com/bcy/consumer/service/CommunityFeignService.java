package com.bcy.consumer.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.List;

@Component
@FeignClient(value = "BCY-CLOUD-HYSTRIX-COMMUNITYPART")
public interface CommunityFeignService {

    @GetMapping("/community/othersInfo")
    Result<JSONObject> getOthersInfo(@RequestParam("id") Long id);

    @DeleteMapping("/community/ask")
    Result<JSONObject> deleteAsk(@RequestParam("id") Long id, @RequestParam("number") Long number);

    @DeleteMapping("/community/ask")
    Result<JSONObject> addAsk(@RequestParam("fromId") Long fromId, @RequestParam("toId") Long toId,
                              @RequestParam("question") String question);

    @DeleteMapping("/community/answer")
    Result<JSONObject> addAnswer(@RequestParam("number") Long number, @RequestParam("id") Long id,
                                 @RequestParam("answer") String answer);

    @GetMapping("/community/atList")
    Result<JSONObject> getAtMessage(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                           @RequestParam("page") Long page);

    @GetMapping("/community/likeList")
    Result<JSONObject> getLikeMessage(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                             @RequestParam("page") Long page);

    @GetMapping("/community/commentList")
    Result<JSONObject> getCommentList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                             @RequestParam("page") Long page);

    @GetMapping("/community/allCounts")
    Result<JSONObject> getAllCounts(@RequestParam("id") Long id);

    @GetMapping("/community/askList")
    Result<JSONObject> getAskList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                         @RequestParam("page") Long page);

    @GetMapping("/community/judgeOnline")
    Result<JSONObject> judgeOnline(@RequestParam("userId") List<Long> userId);

    @DeleteMapping("/community/talkHistory")
    Result<JSONObject> deleteTalkHistory(@RequestParam("id") Long id,
                                                @RequestParam("number") List<Long> number);

    @DeleteMapping("/community/talk")
    Result<JSONObject> deleteTalk(@RequestParam("id") Long id,@RequestParam("toId") Long toId);

    @GetMapping("/community/talkCounts")
    Result<JSONObject> getTalkCounts(@RequestParam("id") Long id,@RequestParam("toId") List<Long> toId) throws ParseException;

    @GetMapping("/community/talkList")
    Result<JSONObject> getTalkList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                          @RequestParam("page") Long page);

    @PostMapping("/community/allRead")
    Result<JSONObject> allRead(@RequestParam("id") Long id,@RequestParam("toId") Long toId);
}
