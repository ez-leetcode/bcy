package com.bcy.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.community.service.TalkService;
import com.bcy.community.service.TimeoutService;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Api(tags = "聊天管理")
@Slf4j
@RestController
public class TalkController {

    @Autowired
    private TimeoutService timeoutService;

    @Autowired
    private TalkService talkService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户问答服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id（list）",required = true,allowMultiple = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "判断用户是否在线（用于头像灰暗显示）",notes = "success：成功 返回data judgeOnlineList：（id：用户id isOnline：是否在线）")
    @GetMapping("/community/judgeOnline")
    public Result<JSONObject> judgeOnline(@RequestParam("userId")List<Long> userId){
        log.info("正在判断用户是否在线" + userId.toString());
        return ResultUtils.getResult(talkService.getJudgeOnline(userId),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "聊天记录编号",required = true,allowMultiple = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "删除用户聊天记录（在1对1聊天页面里类似QQ长按删除）",notes = "success：成功")
    @DeleteMapping("/community/talkHistory")
    public Result<JSONObject> deleteTalkHistory(@RequestParam("id") Long id,
                                                @RequestParam("number") List<Long> number){
        log.info("正在删除用户聊天记录，用户：" + id + " 聊天记录编号：" + number.toString());
        return ResultUtils.getResult(new JSONObject(),talkService.deleteTalkMessage(id, number));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "toId",value = "对方id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "删除用户聊天（在聊天列表里删除用户的聊天栏删除，按QQ逻辑不是vx，删了对面还是能看到）",notes = "existWrong：聊天不存在 success：成功")
    @DeleteMapping("/community/talk")
    public Result<JSONObject> deleteTalk(@RequestParam("id") Long id,@RequestParam("toId") Long toId){
        log.info("正在删除用户聊天，用户：" + id + " 对方id：" + toId);
        return ResultUtils.getResult(new JSONObject(),talkService.deleteTalk(id, toId));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "toId",value = "对方id（list）",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取未读条数和最后一条消息和更新时间（用于聊天列表）",notes = "success：成功 返回data talkCountsList：（id：对方id lastInfo：最后一条消息，用户于列表呈现 noReadCounts：未读条数 updateTime：更新时间 ）")
    @GetMapping("/community/talkCounts")
    public Result<JSONObject> getTalkCounts(@RequestParam("id") Long id,@RequestParam("toId") List<Long> toId) throws ParseException {
        log.info("正在获取聊天列表计数信息，用户：" + id + " 对方id：" + toId.toString());
        return ResultUtils.getResult(talkService.getTalkCounts(id, toId),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取用户聊天列表（最后一次聊天信息 未读数 更新时间在另一个接口）",notes = "success：成功 talkList：（id：用户id username：昵称 photo：头像）")
    @GetMapping("/community/talkList")
    public Result<JSONObject> getTalkList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                          @RequestParam("page") Long page){
        log.info("正在获取用户聊天列表，用户：" + id + " 页面数据量：" + cnt + " 当前页面：" + page);
        return ResultUtils.getResult(talkService.getTalkList(id, cnt, page),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "toId",value = "对方id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "聊天全部已读",notes = "success：成功")
    @PostMapping("/community/allRead")
    public Result<JSONObject> allRead(@RequestParam("id") Long id,@RequestParam("toId") Long toId){
        log.info("正在添加全部聊天为已读，用户：" + id + " 对方id：" + toId);
        return ResultUtils.getResult(new JSONObject(),talkService.allRead(id, toId));
    }

    /*
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "将用户聊天置顶",notes = "existWrong：聊天不存在 success：成功")
    @PostMapping("/community/setTop")
    public Result<JSONObject> setTop(){

    }


     */
}