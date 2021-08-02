package com.bcy.acgpart.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.acgpart.service.QAService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "问答管理类")
@Slf4j
@RestController
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class QAController {

    @Autowired
    private TimeoutService timeoutService;

    @Autowired
    private QAService qaService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户问答服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "问答编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "关注问答",notes = "repeatWrong：已经关注了 existWrong：圈子不存在 success：成功")
    @PostMapping("/acg/followQA")
    public Result<JSONObject> followQA(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在关注问答，用户：" + id + " 问答编号：" + number);
        return ResultUtils.getResult(new JSONObject(),qaService.followQA(id,number));
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "问答编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "取消关注问答",notes = "repeatWrong：已经关注了 existWrong：圈子不存在 success：成功")
    @DeleteMapping("/acg/followQA")
    public Result<JSONObject> disFollowQA(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在关注问答，用户：" + id + " 问答编号：" + number);
        return ResultUtils.getResult(new JSONObject(), qaService.disFollowQA(id,number));
    }


}
