package com.bcy.acgpart.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.bcy.acgpart.service.CircleService;
import com.bcy.acgpart.service.TimeoutService;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "photo",value = "圈子图片",required = true,dataType = "file",paramType = "query")
    })
    @ApiOperation(value = "上传圈子图片",notes = "fileWrong：文件为空 typeWrong：上传格式错误 success：成功 成功后返回json：url（图片url）")
    @PostMapping("/acg/circlePhoto")
    public Result<JSONObject> circlePhotoUpload(@RequestParam("photo")MultipartFile file,@RequestParam("id") Long id){
        log.info("正在上传圈子图片，用户：" + id);
        String url = circleService.circlePhotoUpload(file,id);
        if(url.length() > 12){
            return ResultUtils.getResult(new JSONObject(),url);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url",url);
        return ResultUtils.getResult(jsonObject,"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "circleName",value = "圈子名",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "description",value = "圈子简介",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "photo",value = "圈子图片url",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "创建圈子",notes = "repeatWrong：圈子已存在 success：成功")
    @PostMapping("/acg/circle")
    public Result<JSONObject> createCircle(@RequestParam("id") Long id,@RequestParam("circleName") String circleName,
                                           @RequestParam("description") String description,
                                           @RequestParam("photo") String photo){
        log.info("正在创建圈子，用户：" + id + " 圈子名：" + circleName + " 图片：" + photo + " 描述：" + description);
        return ResultUtils.getResult(new JSONObject(),circleService.createCircle(circleName,description,photo));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "circleName",value = "圈子名",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "获取圈子基本信息",notes = "existWrong：圈子不存在 success：成功 返回data circleInfo：（circleName：圈子名 description：圈子简介 photo：圈子图片url postCounts：圈子发帖数 followCounts：圈子成员数 createTime：创建时间）")
    @GetMapping("/acg/circle")
    public Result<JSONObject> getCircleInfo(@RequestParam("circleName") String circleName){
        log.info("正在获取圈子基本信息，圈子：" + circleName);
        JSONObject jsonObject = circleService.getCircleInfo(circleName);
        if(jsonObject == null){
            return ResultUtils.getResult(new JSONObject(),"existWrong");
        }
        return ResultUtils.getResult(jsonObject,"success");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "circleName",value = "圈子名",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "关注圈子",notes = "repeatWrong：用户已关注 success：成功")
    @PostMapping("/acg/followCircle")
    public Result<JSONObject> followCircle(@RequestParam("id") Long id,@RequestParam("circleName") String circleName){
        log.info("正在关注圈子，用户：" + id + " 圈子：" + circleName);
        return ResultUtils.getResult(new JSONObject(),circleService.followCircle(id,circleName));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "circleName",value = "圈子名",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "取消关注圈子",notes = "repeatWrong：用户未关注 success：成功")
    @DeleteMapping("/acg/followCircle")
    public Result<JSONObject> disFollowCircle(@RequestParam("id") Long id,@RequestParam("circleName") String circleName){
        log.info("正在取消关注圈子，用户：" + id + " 圈子：" + circleName);
        return ResultUtils.getResult(new JSONObject(),circleService.disFollowCircle(id,circleName));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取个人圈子列表",notes = "success：成功 返回data personalCircleList（circleName：圈子名 photo：圈子图片）")
    @GetMapping("/acg/personalCircle")
    public Result<JSONObject> getPersonalCircle(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                                @RequestParam("page") Long page){
        log.info("正在获取个人圈子列表，用户：" + id + " 页面数据量：" + cnt + " 当前页面：" + page);
        return ResultUtils.getResult(circleService.getPersonalCircle(id, cnt, page),"success");
    }

}