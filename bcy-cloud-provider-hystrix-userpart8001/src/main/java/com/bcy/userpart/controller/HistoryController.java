package com.bcy.userpart.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;
import com.bcy.userpart.service.HistoryService;
import com.bcy.userpart.service.TimeoutService;
import com.bcy.utils.ResultUtils;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "用户历史浏览管理类")
@Slf4j
@RestController("/user")
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class HistoryController {

    @Autowired
    private TimeoutService timeoutService;

    @Autowired
    private HistoryService historyService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户帮助服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "keyword",value = "关键词",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "获取历史浏览列表",notes = "success：成功")
    @GetMapping("/historyList")
    public Result<JSONObject> getHistoryList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                             @RequestParam("page") Long page,@RequestParam("keyword") String keyword){
        log.info("正在获取用户历史浏览列表，用户：" + id + " 页面数据量：" + cnt + " 当前页面：" + page + " 关键词：" + keyword);
        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "numbers",value = "历史浏览编号",required = true,allowMultiple = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "批量删除历史浏览",notes = "existWrong：历史浏览不存在 success：成功")
    @DeleteMapping("/history")
    public Result<JSONObject> deleteHistory(@RequestParam("id") Long id,
                                            @RequestParam("numbers")List<Long> numbers){
        log.info("正在批量删除历史浏览，用户：" + id + " 历史编号：" + numbers.toString());
        return ResultUtils.getResult(new JSONObject(),historyService.deleteHistory(id,numbers));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "清空历史浏览",notes = "success：成功")
    @DeleteMapping("/allHistory")
    public Result<JSONObject> deleteAllHistory(@RequestParam("id") Long id){
        log.info("正在清空历史浏览，用户：" + id);
        return ResultUtils.getResult(new JSONObject(),historyService.deleteAllHistory(id));
    }

}
