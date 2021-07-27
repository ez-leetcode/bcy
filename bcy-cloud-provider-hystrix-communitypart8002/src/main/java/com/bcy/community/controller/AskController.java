package com.bcy.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.community.service.AskService;
import com.bcy.community.service.TimeoutService;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "问答管理类")
@Slf4j
@RestController("/community")
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class AskController {

    @Autowired
    private TimeoutService timeoutService;

    @Autowired
    private AskService askService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户问答服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "提问编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "用户删除问答",notes = "existWrong：问答不存在 userWrong：用户不匹配（只有被提问的才能删除） success：成功")
    @DeleteMapping("/ask")
    public Result<JSONObject> deleteAsk(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在删除用户问答，用户：" + id + " 提问编号：" + number);
        return ResultUtils.getResult(new JSONObject(),askService.deleteAsk(id,number));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "fromId",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "toId",value = "被提问用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "question",value = "提问内容",required = true,dataType = "String",paramType = "query")
    })
    @ApiOperation(value = "用户向他人提问",notes = "blackWrong：被对方拉入黑名单 success：成功")
    @DeleteMapping("/ask")
    public Result<JSONObject> addAsk(@RequestParam("fromId") Long fromId,@RequestParam("toId") Long toId,
                                     @RequestParam("question") String question){
        log.info("正在向他人提问，用户：" + fromId + " 对方id：" + toId + " 提问：" + question);
        return ResultUtils.getResult(new JSONObject(),askService.addAsk(fromId,toId,question));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "number",value = "提问编号",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "answer",value = "回答内容",required = true,dataType = "String",paramType = "query")
    })
    @ApiOperation(value = "用户回答问题",notes = "blackWrong：被对方拉入黑名单 success：成功")
    @DeleteMapping("/answer")
    public Result<JSONObject> addAnswer(@RequestParam("number") Long number,@RequestParam("id") Long id,
                                     @RequestParam("answer") String answer){
        return null;
    }

}