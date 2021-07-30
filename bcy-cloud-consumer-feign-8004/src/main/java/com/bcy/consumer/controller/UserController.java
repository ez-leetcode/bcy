package com.bcy.consumer.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.consumer.service.UserFeignService;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserFeignService userFeignService;

    @GetMapping("/user/test")
    public Result<JSONObject> testGet(){
        return userFeignService.getTest();
    }

    @PostMapping("/user/feedback")
    public Result<JSONObject> addFeedback(Long id, String description){
        return userFeignService.addFeedback(id,description);
    }

    @PostMapping("/user/follow")
    public Result<JSONObject> addFollow(Long fromId, Long toId){
        return userFeignService.addFollow(fromId,toId);
    }

    @DeleteMapping("/user/follow")
    public Result<JSONObject> deleteFollow(@RequestParam("fromId") Long fromId,@RequestParam("toId") Long toId){
        return userFeignService.deleteFollow(fromId,toId);
    }

    @PostMapping("/user/judgeFollow")
    public Result<JSONObject> judgeFollow(@RequestParam("fromId") Long fromId,@RequestParam("toId") Long toId){
        return userFeignService.judgeFollow(fromId,toId);
    }

    @GetMapping("/user/followList")
    public Result<JSONObject> getFollow(@RequestParam("id") Long id,@RequestParam("keyword") String keyword,
                                        @RequestParam("cnt") Long cnt,@RequestParam("page") Long page){
        return userFeignService.getFollow(id,keyword,cnt,page);
    }

    @GetMapping("/user/fansList")
    public Result<JSONObject> getFans(@RequestParam("id") Long id,@RequestParam("keyword") String keyword,
                                      @RequestParam("cnt") Long cnt,@RequestParam("page") Long page){
        return userFeignService.getFans(id, keyword, cnt, page);
    }

    @PostMapping("/user/help")
    public Result<JSONObject> addHelp(@RequestParam("question") String question,@RequestParam("answer") String answer,
                                      @RequestParam("type") Integer type){
        return userFeignService.addHelp(question, answer, type);
    }

    @DeleteMapping("/user/help")
    public Result<JSONObject> deleteHelp(@RequestParam("number") Long number){
        return userFeignService.deleteHelp(number);
    }

    @GetMapping("/user/help")
    public Result<JSONObject> getHelp(@RequestParam("number") Long number){
        return userFeignService.getHelp(number);
    }

    @GetMapping("/user/helpList")
    public Result<JSONObject> getHelpList(@RequestParam("cnt") Long cnt,@RequestParam("page") Long page,
                                          @RequestParam("type") Integer type){
        return userFeignService.getHelpList(cnt, page, type);
    }

    @PostMapping("/user/judgeHelp")
    public Result<JSONObject> judgeHelp(@RequestParam("number") Long number,@RequestParam("isSolved") Integer isSolved){
        return userFeignService.judgeHelp(number,isSolved);
    }

    @GetMapping("/user/historyList")
    public Result<JSONObject> getHistoryList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                             @RequestParam("page") Long page,@RequestParam("keyword") String keyword){
        return userFeignService.getHistoryList(id, cnt, page, keyword);
    }

    @PostMapping("/user/photoUpload")
    public Result<JSONObject> userPhotoUpload(@RequestParam("photo") MultipartFile file, Long id){
        return userFeignService.userPhotoUpload(file,id);
    }

    @PatchMapping("/user/personalInfo")
    public Result<JSONObject> changeInfo(@RequestParam("id") Long id,
                                         @RequestParam(value = "sex",required = false) String sex,
                                         @RequestParam(value = "description",required = false) String description,
                                         @RequestParam(value = "username",required = false) String username,
                                         @RequestParam(value = "province",required = false) String province,
                                         @RequestParam(value = "city",required = false) String city,
                                         @RequestParam(value = "birthday",required = false) String birthday){
        return userFeignService.changeInfo(id, sex, description, username, province, city, birthday);
    }

    @GetMapping("/user/personalInfo")
    public Result<JSONObject> getPersonalInfo(@RequestParam("id") Long id){
        return userFeignService.getPersonalInfo(id);
    }

    @PatchMapping("/user/personalSetting")
    public Result<JSONObject> changeSetting(@RequestParam("id") Long id,
                                            @RequestParam(value = "pushComment",required = false) Integer pushComment,
                                            @RequestParam(value = "pushLike",required = false) Integer pushLike,
                                            @RequestParam(value = "pushFans",required = false) Integer pushFans,
                                            @RequestParam(value = "pushSystem",required = false) Integer pushSystem,
                                            @RequestParam(value = "pushInfo",required = false) Integer pushInfo){
        return userFeignService.changeSetting(id, pushComment, pushLike, pushFans, pushSystem, pushInfo);
    }

    @GetMapping("/user/personalSetting")
    public Result<JSONObject> getPersonalSetting(@RequestParam("id") Long id){
        return userFeignService.getPersonalSetting(id);
    }

    @PostMapping("/user/code")
    public Result<JSONObject> sendCode(@RequestParam("phone") String phone, @RequestParam("type") Integer type){
        return userFeignService.sendCode(phone,type);
    }

    @GetMapping("/user/timeout")
    public Result<JSONObject> globalTimeout(){
        return null;
    }

}
