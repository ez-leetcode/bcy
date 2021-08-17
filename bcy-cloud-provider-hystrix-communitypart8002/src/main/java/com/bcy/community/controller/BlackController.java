package com.bcy.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.community.service.BlackService;
import com.bcy.community.service.TimeoutService;
import com.bcy.pojo.Result;
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

@Api(tags = "黑名单管理类")
@Slf4j
@RestController
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class BlackController {

    @Autowired
    private BlackService blackService;

    @Autowired
    private TimeoutService timeoutService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户问答服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "blackId",value = "要被拉黑的id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "把用户加入黑名单",notes = "repeatWrong：重复拉黑 success：成功")
    @PostMapping("/community/black")
    public Result<JSONObject> addBlack(@RequestParam("id") Long id,@RequestParam("blackId") Long blackId){
        log.info("正在把用户拉入黑名单，用户：" + id + " 对方id：" + blackId);
        return ResultUtils.getResult(new JSONObject(),blackService.addBlack(id, blackId));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "blackId",value = "要被取消拉黑的id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "取消拉黑",notes = "repeatWrong：重复拉黑 success：成功")
    @DeleteMapping("/community/black")
    public Result<JSONObject> deleteBlack(@RequestParam("id") Long id,@RequestParam("blackId") Long blackId){
        log.info("正在取消用户拉黑，用户：" + id + " 对方id：" + blackId);
        return ResultUtils.getResult(new JSONObject(),blackService.deleteBlack(id, blackId));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取被屏蔽用户列表（粉丝数在通用接口里拿）",notes = "success：成功 返回data blackList（id：被拉黑用户id username：昵称 photo：头像）")
    @GetMapping("/community/blackList")
    public Result<JSONObject> getBlackList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                           @RequestParam("page") Long page){
        log.info("正在获取用户屏蔽列表，用户：" + id + " 页面数据量：" + cnt + " 当前页面：" + page);
        return ResultUtils.getResult(blackService.getBlackList(id, cnt, page),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "circleName",value = "被屏蔽圈子名",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "添加屏蔽圈子",notes = "repeatWrong：重复屏蔽 success：成功")
    @PostMapping("/community/blackCircle")
    public Result<JSONObject> addBlackCircle(@RequestParam("id") Long id,@RequestParam("circleName") String circleName){
        log.info("正在添加屏蔽圈子，用户：" + id + " 圈子名：" + circleName);
        return ResultUtils.getResult(new JSONObject(),blackService.addBlackCircle(id, circleName));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "circleName",value = "要取消屏蔽圈子名",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "取消屏蔽圈子",notes = "repeatWrong：重复取消屏蔽 success：成功")
    @DeleteMapping("/community/blackCircle")
    public Result<JSONObject> deleteBlackCircle(@RequestParam("id") Long id,@RequestParam("circleName") String circleName){
        log.info("正在取消屏蔽圈子，用户：" + id + " 圈子名：" + circleName);
        return ResultUtils.getResult(new JSONObject(),blackService.deleteBlackCircle(id, circleName));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "circleName",value = "圈子名",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "判断圈子是否被屏蔽",notes = "success：成功 返回data isBlack：1被屏蔽 0未被屏蔽")
    @GetMapping("/community/judgeBlackCircle")
    public Result<JSONObject> judgeBlackCircle(@RequestParam("id") Long id,@RequestParam("circleName") String circleName){
        log.info("正在判断圈子是否被屏蔽，用户：" + id + " 圈子名：" + circleName);
        return ResultUtils.getResult(blackService.judgeBlackCircle(id, circleName),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "toId",value = "对方id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "判断用户是否被屏蔽",notes = "success：成功 返回data isBlack：1被屏蔽 0未被屏蔽")
    @GetMapping("/community/judgeBlack")
    public Result<JSONObject> judgeBlack(@RequestParam("id") Long id,@RequestParam("toId") Long toId){
        log.info("正在判断用户是否被屏蔽，用户：" + id + " 对方id：" + toId);
        return ResultUtils.getResult(blackService.judgeBlack(id, toId),"success");
    }

}