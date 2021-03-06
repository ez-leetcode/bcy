package com.bcy.acgpart.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.bcy.acgpart.service.CircleService;
import com.bcy.acgpart.service.TimeoutService;
import com.bcy.pojo.Result;
import com.bcy.utils.BngelUtils;
import com.bcy.utils.ResultUtils;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Api(tags = "圈子管理类")
@Slf4j
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class CircleController {

    @Autowired
    private TimeoutService timeoutService;

    @Autowired
    private CircleService circleService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户问答服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "photo",value = "圈子图片",required = true,dataType = "file",paramType = "query")
    })
    @ApiOperation(value = "上传圈子图片P",notes = "fileWrong：文件为空 typeWrong：上传格式错误 success：成功 成功后返回json：url（图片url）")
    @PostMapping("/acg/circlePhoto")
    public Result<JSONObject> circlePhotoUpload(@RequestParam("photo")MultipartFile file,@RequestParam("id") String id){
        log.info("正在上传圈子图片，用户：" + id);
        String realId = BngelUtils.getRealFileName(id);
        log.info(realId);
        String url = circleService.circlePhotoUpload(file,Long.parseLong(realId));
        if(url.length() < 12){
            return ResultUtils.getResult(new JSONObject(),url);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url",url);
        return ResultUtils.getResult(jsonObject,"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "circleName",value = "圈子名",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "description",value = "圈子简介",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "photo",value = "圈子图片url",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "nickName",value = "成员昵称",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "创建圈子P",notes = "repeatWrong：圈子已存在 success：成功")
    @PostMapping("/acg/circle")
    public Result<JSONObject> createCircle(@RequestParam("id") Long id,@RequestParam("circleName") String circleName,
                                           @RequestParam("description") String description,
                                           @RequestParam("photo") String photo,@RequestParam("nickName") String nickName){
        log.info("正在创建圈子，用户：" + id + " 圈子名：" + circleName + " 图片：" + photo + " 描述：" + description + " 成员昵称：" + nickName);
        return ResultUtils.getResult(new JSONObject(),circleService.createCircle(circleName,description,photo,nickName));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "circleName",value = "圈子名",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "获取圈子基本信息P",notes = "existWrong：圈子不存在 success：成功 返回data circleInfo：（circleName：圈子名 description：圈子简介 photo：圈子图片url nickName：成员昵称 postCounts：圈子发帖数 followCounts：圈子成员数 createTime：创建时间）")
    @GetMapping("/acg/circle")
    public Result<JSONObject> getCircleInfo(@RequestParam("circleName") String circleName){
        log.info("正在获取圈子基本信息，圈子：" + circleName);
        JSONObject jsonObject = circleService.getCircleInfo(circleName);
        if(jsonObject == null){
            return ResultUtils.getResult(new JSONObject(),"existWrong");
        }
        return ResultUtils.getResult(jsonObject,"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "circleName",value = "圈子名",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "关注圈子P",notes = "repeatWrong：用户已关注 success：成功")
    @PostMapping("/acg/followCircle")
    public Result<JSONObject> followCircle(@RequestParam("id") Long id,@RequestParam("circleName") String circleName){
        log.info("正在关注圈子，用户：" + id + " 圈子：" + circleName);
        return ResultUtils.getResult(new JSONObject(),circleService.followCircle(id,circleName));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "circleName",value = "圈子名",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "取消关注圈子P",notes = "repeatWrong：用户未关注 success：成功")
    @DeleteMapping("/acg/followCircle")
    public Result<JSONObject> disFollowCircle(@RequestParam("id") Long id,@RequestParam("circleName") String circleName){
        log.info("正在取消关注圈子，用户：" + id + " 圈子：" + circleName);
        return ResultUtils.getResult(new JSONObject(),circleService.disFollowCircle(id,circleName));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取个人圈子列表P",notes = "success：成功 返回data personalCircleList（circleName：圈子名 photo：圈子图片 description：圈子描述）")
    @GetMapping("/acg/personalCircle")
    public Result<JSONObject> getPersonalCircle(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                                @RequestParam("page") Long page){
        log.info("正在获取个人圈子列表，用户：" + id + " 页面数据量：" + cnt + " 当前页面：" + page);
        return ResultUtils.getResult(circleService.getPersonalCircle(id, cnt, page),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "circleNames",value = "圈子名",allowMultiple = true,required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "批量判断圈子是否关注（一个也用这个接口）",notes = "success：成功 judgeCircleList（circleName：圈子名 isFollow：1关注 0未关注）")
    @GetMapping("/acg/judgeCircle")
    public Result<JSONObject> judgeCircle(@RequestParam("id") Long id, @RequestParam("circleNames")List<String> circleNames){
        log.info("正在判断圈子是否关注，用户：" + id);
        return ResultUtils.getResult(circleService.judgeCircleList(id, circleNames),"success");
    }


    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "keyword",value = "关键词（不要带空）",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "搜索圈子（屏蔽圈子搜不到，所以要带id）",notes = "success：成功 返回searchCircleList（circleName：圈子名 photo：圈子图片 description：圈子描述）")
    @GetMapping("/acg/searchCircle")
    public Result<JSONObject> searchCircle(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                           @RequestParam("page") Long page,@RequestParam("keyword") String keyword){
        log.info("正在搜索圈子，用户：" + id + " 页面数据量：" + cnt + " 当前页面：" + page + " 关键词：" + keyword);
        return ResultUtils.getResult(circleService.searchCircle(id,cnt,page,keyword),"success");
    }

    //@HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "circleName",value = "圈子名",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "type",value = "排序方式（0：按时间 1：按热度）",required = true,dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取圈子下cos列表",notes = "返回data circleCosList（number：cos编号 id：发布者id username：昵称 " +
            "photo：头像 description：描述内容 cosPhoto：图片列表（list） createTime：发布时间")
    @GetMapping("/acg/circleCosList")
    public Result<JSONObject> getCircleCosList(@RequestParam("circleName") String circleName,@RequestParam("type") Integer type,
                                               @RequestParam("cnt") Long cnt,@RequestParam("page") Long page){
        log.info("正在获取圈子下的cos列表，圈子：" + circleName + " 排序方式：" + type + " 页面数据量：" + cnt + " 当前页面：" + page);
        return ResultUtils.getResult(circleService.getCircleCosList(circleName,type,cnt,page),"success");
    }

}