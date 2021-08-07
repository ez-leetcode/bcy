package com.bcy.userpart.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;
import com.bcy.userpart.service.FeedbackService;
import com.bcy.userpart.service.TimeoutService;
import com.bcy.utils.ResultUtils;
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

@Api(tags = "用户反馈管理类")
@Slf4j
@RestController
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private TimeoutService timeoutService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户反馈服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "description",value = "反馈内容",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "添加用户反馈P",notes = "repeatWrong：短期反馈太多次（8小时内最多3次） success：成功")
    @PostMapping("/user/feedback")
    public Result<JSONObject> addFeedback(@RequestParam("id") Long id,@RequestParam("description") String description){
        log.info("正在添加用户反馈，id：" + id + " description：" + description);
        return ResultUtils.getResult(new JSONObject(), feedbackService.addFeedback(id,description));
    }

}