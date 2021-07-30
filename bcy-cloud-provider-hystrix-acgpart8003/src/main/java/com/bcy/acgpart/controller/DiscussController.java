package com.bcy.acgpart.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.acgpart.service.DiscussService;
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

import java.util.List;

@RestController
@Api(tags = "圈子下面的讨论管理类")
@Slf4j
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class DiscussController {

    @Autowired
    private TimeoutService timeoutService;

    @Autowired
    private DiscussService discussService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户问答服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "numbers",value = "讨论编号",required = true,allowMultiple = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "批量删除讨论（管理员用，如果违反规定就删除）（只有一个也扔这个接口~）",notes = "existWrong：讨论不存在 success：成功")
    @DeleteMapping("/acg/discuss")
    public Result<JSONObject> deleteDiscuss(@RequestParam("numbers")List<Long> numbers){
        log.info("正在批量删除讨论");
        log.info(numbers.toString());
        return ResultUtils.getResult(new JSONObject(), discussService.deleteDiscuss(numbers));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "title",value = "标题",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "description",value = "描述",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "发起讨论",notes = "success：成功")
    @PostMapping("/acg/discuss")
    public Result<JSONObject> createDiscuss(@RequestParam("id") Long id,@RequestParam("title") String title,
                                            @RequestParam("description") String description){
        log.info("正在发起讨论，用户：" + id + " 标题：" + title + " 描述：" + description);
        return null;
    }


}