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
import org.springframework.web.bind.annotation.*;

@Api(tags = "粉丝or关注管理类")
@Slf4j
@RestController
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
    @ApiOperation(value = "关注别的用户（关注在线的话，会有推送）P",notes = "repeatWrong：已成功关注（重复请求） success：成功")
    @PostMapping("/user/follow")
    public Result<JSONObject> addFollow(@RequestParam("fromId") Long fromId,@RequestParam("toId") Long toId){
        log.info("正在添加关注，用户：" + fromId + " 被关注用户：" + toId);
        return ResultUtils.getResult(new JSONObject(),fansService.addFollow(fromId,toId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "fromId",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "toId",value = "被关注用户id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "取消关注别的用户P",notes = "repeatWrong：已成功取消关注（重复请求） success：成功")
    @DeleteMapping("/user/follow")
    public Result<JSONObject> deleteFollow(@RequestParam("fromId") Long fromId,@RequestParam("toId") Long toId){
        log.info("正在取消关注，用户：" + fromId + " 被关注用户：" + toId);
        return ResultUtils.getResult(new JSONObject(),fansService.deleteFollow(fromId,toId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "fromId",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "toId",value = "对方用户id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "判断用户的关注情况P",notes = "返回data中 status：0未关注 1已关注 2已相互关注 3被关注（用来判断关注后是互粉还是单纯关注）")
    @PostMapping("/user/judgeFollow")
    public Result<JSONObject> judgeFollow(@RequestParam("fromId") Long fromId,@RequestParam("toId") Long toId){
        log.info("正在判断用户的关注情况，用户id：" + fromId + " 对方id：" + toId);
        return ResultUtils.getResult(fansService.judgeFollow(fromId,toId),"success");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "keyword",value = "关键词（查全部给空或不带）",dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取自己的关注列表（还要显示关注情况的话，请用judgeFollow那个接口）P",notes = "success：成功 返回data followList：关注列表（id：用户id sex：性别 username：昵称 photo：头像url fansCounts：粉丝数） counts：数据总量 pages：页面总数")
    @GetMapping("/user/followList")
    public Result<JSONObject> getFollow(@RequestParam("id") Long id,@RequestParam(value = "keyword",required = false) String keyword,
                                        @RequestParam("cnt") Long cnt,@RequestParam("page") Long page){
        log.info("正在获取自己全部的关注列表，用户：" + id + " 关键词：" + keyword + " 数据量：" + cnt + " 页面：" + page);
        return ResultUtils.getResult(fansService.getFollowList(id, keyword, cnt, page),"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "keyword",value = "关键词（查全部给空或不带）",dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取自己的粉丝列表（还要显示关注情况的话，请用judgeFollow那个接口）P",notes = "success：成功 返回data fansList：粉丝列表（id：用户id sex：性别 username：昵称 photo：头像url fansCounts：粉丝数） counts：数据总量 pages：页面总数")
    @GetMapping("/user/fansList")
    public Result<JSONObject> getFans(@RequestParam("id") Long id,@RequestParam(value = "keyword",required = false) String keyword,
                                        @RequestParam("cnt") Long cnt,@RequestParam("page") Long page){
        log.info("正在获取自己全部的粉丝列表，用户：" + id + " 关键词：" + keyword + " 数据量：" + cnt + " 页面：" + page);
        return ResultUtils.getResult(fansService.getFansList(id,keyword,cnt,page),"success");
    }


}