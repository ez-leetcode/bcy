package com.bcy.consumer.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
    Result<JSONObject> getFollow(@RequestParam("id") Long id,@RequestParam(value = "keyword",required = false) String keyword,
                                        @RequestParam("cnt") Long cnt,@RequestParam("page") Long page);

    @GetMapping("/user/fansList")
    Result<JSONObject> getFans(@RequestParam("id") Long id,@RequestParam(value = "keyword",required = false) String keyword,
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

    @PostMapping(value = "/user/photoUpload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<JSONObject> userPhotoUpload(@RequestPart("photo") MultipartFile file, @RequestParam("id") String id);

    @PutMapping("/user/personalInfo")
    Result<JSONObject> changeInfo(@RequestParam("id") Long id,
                                         @RequestParam(value = "sex",required = false) String sex,
                                         @RequestParam(value = "description",required = false) String description,
                                         @RequestParam(value = "username",required = false) String username,
                                         @RequestParam(value = "province",required = false) String province,
                                         @RequestParam(value = "city",required = false) String city,
                                         @RequestParam(value = "birthday",required = false) String birthday);

    @GetMapping("/user/personalInfo")
    Result<JSONObject> getPersonalInfo(@RequestParam(value = "id",required = false) Long id,@RequestParam(value = "phone",required = false) String phone);

    @PutMapping("/user/personalSetting")
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

    @GetMapping("/user/historyList")
    Result<JSONObject> getHistoryList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                             @RequestParam("page") Long page);

    @GetMapping("/user/qaHistoryList")
    Result<JSONObject> getQaHistoryList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                               @RequestParam("page") Long page);

    @DeleteMapping("/user/qaHistory")
    Result<JSONObject> deleteQaHistory(@RequestParam("id") Long id,
                                              @RequestParam("numbers") List<Long> numbers);

    @DeleteMapping("/user/allQaHistory")
    Result<JSONObject> deleteAllQaHistory(@RequestParam("id") Long id);

    @DeleteMapping("/user/history")
    Result<JSONObject> deleteHistory(@RequestParam("id") Long id,
                                            @RequestParam("numbers")List<Long> numbers);

    @DeleteMapping("/user/allHistory")
    Result<JSONObject> deleteAllHistory(@RequestParam("id") Long id);

    @GetMapping("/user/userCounts")
    Result<JSONObject> getUserCounts(@RequestParam("userId")List<Long> userId);

    @PostMapping("/user/judgeNew")
    Result<JSONObject> judgeNewUser(@RequestParam("id") Long id);

    @PostMapping("/user/setPassword")
    Result<JSONObject> setPassword(@RequestParam("id") Long id,@RequestParam("password") String password);

    @PostMapping("/user/changePhone")
    Result<JSONObject> changePhone(@RequestParam("id") Long id,@RequestParam("phone") String phone,
                                          @RequestParam("code") String code);
}