package com.bcy.userpart.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;
import com.bcy.userpart.service.FansService;
import com.bcy.userpart.service.TimeoutService;
import com.bcy.utils.ResultUtils;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "粉丝or关注管理类")
@Slf4j
@RestController("/user")
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class FansController {

    @Autowired
    private FansService fansService;

    @Autowired
    private TimeoutService timeoutService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("粉丝/关注服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "fromId",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "toId",value = "被关注用户id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "关注别的用户",notes = "repeatWrong：已成功关注（重复请求） success：成功")
    @PostMapping("/follow")
    public Result<JSONObject> addFollow(@RequestParam("fromId") Long fromId,@RequestParam("toId") Long toId){
        log.info("正在添加关注，用户：" + fromId + " 被关注用户：" + toId);
        return ResultUtils.getResult(new JSONObject(),fansService.addFollow(fromId,toId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "fromId",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "toId",value = "被关注用户id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "取消关注别的用户",notes = "repeatWrong：已成功取消关注（重复请求） success：成功")
    @DeleteMapping("/follow")
    public Result<JSONObject> deleteFollow(@RequestParam("fromId") Long fromId,@RequestParam("toId") Long toId){
        log.info("正在取消关注，用户：" + fromId + " 被关注用户：" + toId);
        return ResultUtils.getResult(new JSONObject(),fansService.deleteFollow(fromId,toId));
    }

}
