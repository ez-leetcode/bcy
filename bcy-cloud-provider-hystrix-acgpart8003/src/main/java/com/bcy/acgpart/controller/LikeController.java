package com.bcy.acgpart.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.acgpart.service.LikeService;
import com.bcy.acgpart.service.TimeoutService;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "喜欢(只有cos)管理类")
@Slf4j
@RestController
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private TimeoutService timeoutService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户问答服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "numbers",value = "cos编号（list）",required = true,allowMultiple = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取是否被喜欢",notes = "success：成功 judgeLikeList（number：编号 status 0：未点赞 1：已点赞）")
    @GetMapping("/acg/judgeLikes")
    public Result<JSONObject> judgeLikes(@RequestParam("id") Long id, @RequestParam("numbers")List<Long> numbers){
        log.info("正在判断是否被喜欢，用户：" + id + " cos编号：" + numbers.toString());
        return ResultUtils.getResult(likeService.getLikeStatus(id,numbers),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query"),
    })
    @ApiOperation(value = "获取喜欢列表（点赞收藏内容请调用另外接口）",notes = "success：成功 返回data likeCosList（number：cos编号 id：发布用户id username：发布用户昵称 photo：发布用户头像" +
            " description：内容 cosPhoto：照片列表（list） createTime：发布时间）")
    @GetMapping("/acg/likeList")
    public Result<JSONObject> getLikeList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                          @RequestParam("page") Long page){
        log.info("正在获取喜欢列表，用户：" + id + " 页面数据量：" + cnt + " 当前页面：" + page);
        return ResultUtils.getResult(likeService.getLikeList(id,cnt,page),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "cos编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "添加喜欢cos",notes = "existWrong：cos不存在 repeatWrong：重复喜欢 success：成功")
    @PostMapping("/acg/likeCos")
    public Result<JSONObject> likeCos(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在添加喜欢cos，用户：" + id + " cos编号：" + number);
        return ResultUtils.getResult(new JSONObject(),likeService.addLike(id,number));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "cos编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "取消喜欢cos",notes = "existWrong：cos不存在 repeatWrong：未喜欢 success：成功")
    @DeleteMapping("/acg/likeCos")
    public Result<JSONObject> deleteLikeCos(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在取消喜欢cos，用户：" + id + " cos编号：" + number);
        return ResultUtils.getResult(new JSONObject(),likeService.deleteLike(id,number));
    }

}