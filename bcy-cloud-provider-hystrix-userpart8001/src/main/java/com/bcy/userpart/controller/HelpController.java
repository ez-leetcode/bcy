package com.bcy.userpart.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;
import com.bcy.userpart.service.HelpService;
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

@Api(tags = "用户帮助管理类")
@Slf4j
@RestController("/user")
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class HelpController {

    @Autowired
    private TimeoutService timeoutService;

    @Autowired
    private HelpService helpService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户帮助服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "question",value = "帮助问题",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "answer",value = "回答",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "type",value = "问题分类（常用问题给0）",required = true,dataType = "Integer",paramType = "query")
    })
    @ApiOperation(value = "添加帮助（我们自己后台添加用啦~）",notes = "success：成功")
    @PostMapping("/help")
    public Result<JSONObject> addHelp(@RequestParam("question") String question,@RequestParam("answer") String answer,
                                      @RequestParam("type") Integer type){
        log.info("正在添加用户帮助，问题：" + question + " 回答：" + answer + " 问题分类：" + type);
        return ResultUtils.getResult(new JSONObject(), helpService.addHelp(question,answer,type));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "number",value = "帮助编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "移除帮助（后台专用）",notes = "existWrong：帮助不存在 success：成功")
    @DeleteMapping("/help")
    public Result<JSONObject> deleteHelp(@RequestParam("number") Long number){
        log.info("正在移除帮助，编号：" + number);
        return ResultUtils.getResult(new JSONObject(), helpService.deleteHelp(number));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "number",value = "帮助编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取帮助的具体内容",notes = "existWrong：帮助不存在 success：成功 返回data： help（number：帮助编号 question：问题 answer：解决方案）")
    @GetMapping("/help")
    public Result<JSONObject> getHelp(@RequestParam("number") Long number){
        log.info("正在获取帮助的具体内容，编号：" + number);
        JSONObject jsonObject = helpService.getHelp(number);
        if(jsonObject == null){
            return ResultUtils.getResult(new JSONObject(),"existWrong");
        }
        return ResultUtils.getResult(jsonObject,"success");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "type",value = "帮助类型",required = true,dataType = "Integer",paramType = "query")
    })
    @ApiOperation(value = "获取用户帮助列表",notes = "返回data helpList：帮助列表（number：帮助编号 question：问题）cnts：数据总量，pages：页面总数")
    @GetMapping("/helpList")
    public Result<JSONObject> getHelpList(@RequestParam("cnt") Long cnt,@RequestParam("page") Long page,
                                          @RequestParam("type") Integer type){
        log.info("正在获取用户帮助列表，页面数据量：" + cnt + " 当前页面：" + page + " 帮助类型：" + type);
        return ResultUtils.getResult(helpService.getHelpList(cnt,page,type),"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "number",value = "帮助编号",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "isSolved",value = "是否已解决（0：未解决 1：已解决）",required = true,dataType = "Integer",paramType = "query")
    })
    @ApiOperation(value = "添加帮助的已解决或未解决",notes = "existWrong：帮助不存在 success：成功")
    @PostMapping("/judgeHelp")
    public Result<JSONObject> judgeHelp(@RequestParam("number") Long number,@RequestParam("isSolved") Integer isSolved){
        log.info("正在添加帮助的已解决或未解决，帮助编号：" + number + " 是否已解决：" + isSolved);
        return ResultUtils.getResult(new JSONObject(),helpService.addSolve(number,isSolved));
    }

}