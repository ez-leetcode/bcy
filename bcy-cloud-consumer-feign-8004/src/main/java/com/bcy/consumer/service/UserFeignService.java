package com.bcy.consumer.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

//根据eureka里的微服务客户端信息转交给对应微服务提供者
//feign自带负载均衡，会在微服务提供者之间进行轮询或其他负载均衡办法
@Component
@FeignClient(value = "BCY-CLOUD-HYSTRIX-USERPART")
public interface UserFeignService {

    @GetMapping("/user/test")
    Result<JSONObject> getTest();

    @PostMapping("/user/feedback")
    Result<JSONObject> addFeedback(@RequestParam("id") Long id, @RequestParam("description") String description);

    @PostMapping("/user/follow")
    Result<JSONObject> addFollow(@RequestParam("fromId") Long fromId,@RequestParam("toId") Long toId);

    @DeleteMapping("/user/follow")
    Result<JSONObject> deleteFollow(@RequestParam("fromId") Long fromId,@RequestParam("toId") Long toId);

    @PostMapping("/user/judgeFollow")
    Result<JSONObject> judgeFollow(@RequestParam("fromId") Long fromId,@RequestParam("toId") Long toId);

    @GetMapping("/user/followList")
    Result<JSONObject> getFollow(@RequestParam("id") Long id,@RequestParam("keyword") String keyword,
                                        @RequestParam("cnt") Long cnt,@RequestParam("page") Long page);

    @GetMapping("/user/fansList")
    Result<JSONObject> getFans(@RequestParam("id") Long id,@RequestParam("keyword") String keyword,
                                      @RequestParam("cnt") Long cnt,@RequestParam("page") Long page);

    @PostMapping("/user/help")
    Result<JSONObject> addHelp(@RequestParam("question") String question,@RequestParam("answer") String answer,
                                      @RequestParam("type") Integer type);
    @DeleteMapping("/user/help")
    Result<JSONObject> deleteHelp(@RequestParam("number") Long number);
    @GetMapping("/user/help")
    Result<JSONObject> getHelp(@RequestParam("number") Long number);

    @GetMapping("/user/helpList")
    Result<JSONObject> getHelpList(@RequestParam("cnt") Long cnt,@RequestParam("page") Long page,
                                          @RequestParam("type") Integer type);

    @PostMapping("/user/judgeHelp")
    Result<JSONObject> judgeHelp(@RequestParam("number") Long number,@RequestParam("isSolved") Integer isSolved);

    @GetMapping("/user/historyList")
    Result<JSONObject> getHistoryList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                             @RequestParam("page") Long page,@RequestParam("keyword") String keyword);

    @PostMapping("/user/photoUpload")
    Result<JSONObject> userPhotoUpload(@RequestParam("photo") MultipartFile file, Long id);
    @PatchMapping("/user/personalInfo")
    Result<JSONObject> changeInfo(@RequestParam("id") Long id,
                                         @RequestParam(value = "sex",required = false) String sex,
                                         @RequestParam(value = "description",required = false) String description,
                                         @RequestParam(value = "username",required = false) String username,
                                         @RequestParam(value = "province",required = false) String province,
                                         @RequestParam(value = "city",required = false) String city,
                                         @RequestParam(value = "birthday",required = false) String birthday);

    @GetMapping("/user/personalInfo")
    Result<JSONObject> getPersonalInfo(@RequestParam("id") Long id);

    @PatchMapping("/user/personalSetting")
    Result<JSONObject> changeSetting(@RequestParam("id") Long id,
                                            @RequestParam(value = "pushComment",required = false) Integer pushComment,
                                            @RequestParam(value = "pushLike",required = false) Integer pushLike,
                                            @RequestParam(value = "pushFans",required = false) Integer pushFans,
                                            @RequestParam(value = "pushSystem",required = false) Integer pushSystem,
                                            @RequestParam(value = "pushInfo",required = false) Integer pushInfo);

    @GetMapping("/user/personalSetting")
    Result<JSONObject> getPersonalSetting(@RequestParam("id") Long id);

    @PostMapping("/user/code")
    Result<JSONObject> sendCode(@RequestParam("phone") String phone, @RequestParam("type") Integer type);
}