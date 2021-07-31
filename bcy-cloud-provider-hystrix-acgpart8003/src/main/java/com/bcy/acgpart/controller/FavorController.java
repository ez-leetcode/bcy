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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @ApiImplicitParam(name = "keyword",value = "关键词",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取收藏列表",notes = "favorList：success：成功")
    @GetMapping("/acg/favorList")
    public Result<JSONObject> getFavorList(@RequestParam("id") Long id,@RequestParam("keyword") String keyword,
                                           @RequestParam("page") Long page,@RequestParam("cnt") Long cnt){
        log.info("正在获取收藏列表，用户：" + id + " 关键词：" + keyword + " 页面数据量：" + cnt + " 当前页面：" + page);
        return ResultUtils.getResult(favorService.getFavorList(id, keyword, page, cnt),"success");
    }


}