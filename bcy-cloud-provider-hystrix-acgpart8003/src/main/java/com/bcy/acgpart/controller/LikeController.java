package com.bcy.acgpart.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.acgpart.service.TimeoutService;
import com.bcy.pojo.Result;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "喜欢（帖子或讨论）管理类")
@Slf4j
@RestController
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class LikeController {

    @Autowired
    private TimeoutService timeoutService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户问答服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "numbers",value = "讨论编号",required = true,allowMultiple = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取是否被喜欢",notes = "success：成功")
    @GetMapping("/acg/judgeLikes")
    public Result<JSONObject> judgeLikes(@RequestParam("id") Long id, @RequestParam("numbers")List<Long> numbers){
        log.info("正在判断是否被喜欢，用户：" + id);
        log.info(numbers.toString());
        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "keyword",value = "关键词",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "获取喜欢列表",notes = "success：成功")
    @GetMapping("/acg/likeList")
    public Result<JSONObject> getLikeList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                          @RequestParam("page") Long page,@RequestParam("keyword") String keyword){
        log.info("正在获取喜欢列表，用户：" + id + " 页面数据量：" + cnt + " 当前页面：" + page + " 关键词：" + keyword);
        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "帖子编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "添加喜欢帖子")
    @PostMapping("/acg/like")
    public Result<JSONObject> likeDiscuss(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在添加喜欢帖子，用户：" + id + " 帖子编号：" + number);
        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "帖子编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "取消喜欢帖子")
    @PostMapping("/acg/like")
    public Result<JSONObject> dislikeDiscuss(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在取消喜欢帖子，用户：" + id + " 帖子编号：" + number);
        return null;
    }

}