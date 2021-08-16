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

@Api(tags = "主页管理类（注意是别人的主页！） 里面还有搜索别的用户的接口")
@Slf4j
@RestController
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
    @ApiOperation(value = "获取他人的主要信息（查看他人信息那里用）P",notes = "existWrong：用户不存在 success：成功 返回data userInfo（id：用户id username：昵称 photo：头像URL description：自我介绍 sex：性别 followCounts：关注数 fansCounts：粉丝数）")
    @GetMapping("/community/othersInfo")
    public Result<JSONObject> getOthersInfo(@RequestParam("id") Long id){
        log.info("正在获取他人信息，用户：" + id);
        JSONObject jsonObject = homePageService.getOthersInfo(id);
        if(jsonObject == null){
            return ResultUtils.getResult(new JSONObject(),"existWrong");
        }
        return ResultUtils.getResult(jsonObject,"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "该用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取他人页面下的所有cos",notes = "返回data cosUserList（number：cos编号 id：发布用户id username：昵称 photo：头像 " +
            "cosPhoto：cos图片（list） label：标签（list）  description：内容 createTime：发布时间）")
    @GetMapping("/community/cosList")
    public Result<JSONObject> getUserCosList(@RequestParam("userId") Long userId,@RequestParam("cnt") Long cnt,
                                             @RequestParam("page") Long page){
        log.info("正在获取他人页面下所有cos，用户：" + userId + " 页面数据量：" + cnt + " 当前页面：" + page);
        return ResultUtils.getResult(homePageService.getUserCosList(userId,cnt,page),"success");
    }




    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "keyword",value = "搜索关键词（不能给空）",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "搜索用户（默认搜索不到自己）",notes = "success：成功 返回data searchUserList（id：用户id username：昵称 photo：头像 粉丝数和是否关注在统一接口里获取 ）")
    @GetMapping("/community/searchUser")
    public Result<JSONObject> searchUser(@RequestParam("id") Long id,@RequestParam("page") Long page,
                                         @RequestParam("cnt") Long cnt,@RequestParam("keyword") String keyword){
        log.info("正在搜索用户，用户：" + id + " 当前页面：" + page + " 页面数据量：" + cnt + " 关键词：" + keyword);
        return ResultUtils.getResult(homePageService.searchUser(id, page, cnt, keyword),"success");
    }

}