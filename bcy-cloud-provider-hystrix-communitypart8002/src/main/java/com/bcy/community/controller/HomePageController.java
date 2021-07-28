package com.bcy.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.community.service.HomePageService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "他人主页管理类（注意是别人的主页！）")
@Slf4j
@RestController("/community")
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class HomePageController {

    @Autowired
    private HomePageService homePageService;

    @Autowired
    private TimeoutService timeoutService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户问答服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "他人用户id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取他人的主要信息（查看他人信息那里用）",notes = "existWrong：用户不存在 success：成功 返回data userInfo（id：用户id username：昵称 photo：头像URL description：自我介绍 sex：性别 followCounts：关注数 fansCounts：粉丝数）")
    @GetMapping("/othersInfo")
    public Result<JSONObject> getOthersInfo(@RequestParam("id") Long id){
        log.info("正在获取他人信息，用户：" + id);
        JSONObject jsonObject = homePageService.getOthersInfo(id);
        if(jsonObject == null){
            return ResultUtils.getResult(new JSONObject(),"existWrong");
        }
        return ResultUtils.getResult(jsonObject,"success");
    }

}
