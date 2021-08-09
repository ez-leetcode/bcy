package com.bcy.acgpart.controller;


import com.alibaba.fastjson.JSONObject;
import com.bcy.acgpart.service.FavorService;
import com.bcy.acgpart.service.TimeoutService;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "收藏管理类")
@Slf4j
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class FavorController {

    @Autowired
    private FavorService favorService;

    @Autowired
    private TimeoutService timeoutService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户问答服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取收藏列表（文章/cos）P",notes = "success：成功 返回favorList：（cos_number：cos编号 id：cos用户id " +
            "username：用户昵称 photo：用户头像 cosPhoto：cos图片（list） create_time：cos发布时间）")
    @GetMapping("/acg/favorList")
    public Result<JSONObject> getFavorList(@RequestParam("id") Long id,
                                           @RequestParam("page") Long page,@RequestParam("cnt") Long cnt){
        log.info("正在获取收藏列表，用户：" + id + " 页面数据量：" + cnt + " 当前页面：" + page);
        return ResultUtils.getResult(favorService.getFavorList(id, page, cnt),"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "cos文章编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "添加收藏（文章/cos）P",notes = "existWrong：cos不存在 repeatWrong：重复收藏 success：成功")
    @PostMapping("/acg/favorCos")
    public Result<JSONObject> addFavorCos(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在添加cos收藏，用户：" + id + " cos编号：" + number);
        return ResultUtils.getResult(new JSONObject(), favorService.addFavor(id, number));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "cos文章编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "取消收藏（文章/cos）P",notes = "existWrong：cos不存在 repeatWrong：未收藏 success：成功")
    @DeleteMapping("/acg/favorCos")
    public Result<JSONObject> deleteFavorCos(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在取消cos收藏，用户：" + id + " cos编号：" + number);
        return ResultUtils.getResult(new JSONObject(), favorService.deleteFavor(id, number));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "cos文章编号（list）",allowMultiple = true,required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "判断cos是否已经收藏P",notes = "success：成功 返回data：judgeFavorList（id：用户id  status：0未收藏 1已收藏）")
    @PostMapping("/acg/judgeFavor")
    public Result<JSONObject> judgeFavor(@RequestParam("id") Long id, @RequestParam("number")List<Long> number){
        log.info("正在判断是否已经收藏，用户id：" + id + " 编号：" + number.toString());
        return ResultUtils.getResult(favorService.judgeFavor(id,number),"success");
    }

}