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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @ApiImplicitParam(name = "photo",required = true,dataType = "file",paramType = "query")
    })
    @ApiOperation(value = "问答图片上传",notes = "fileWrong：文件为空 typeWrong：文件类型错误 success：成功（返回json带url）")
    @PostMapping("/acg/photoUpload")
    public Result<JSONObject> photoUpload(@RequestParam("photo") MultipartFile file){
        log.info("正在上传帖子图片");
        String url = qaService.photoUpload(file);
        if(url.length() < 12){
            return ResultUtils.getResult(new JSONObject(),url);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url",url);
        return ResultUtils.getResult(jsonObject,"success");
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
            @ApiImplicitParam(name = "number",value = "问答编号",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "keyword",value = "关键词",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "获取关注该问题的用户列表",notes = "success：成功 返回data：followQAList（id：用户id username：用户昵称 fansCounts：粉丝数 photo：头像url）")
    @GetMapping("/acg/followQAList")
    public Result<JSONObject> getFollowQAList(@RequestParam("id") Long id,@RequestParam("number") Long number,
                                              @RequestParam("cnt") Long cnt,@RequestParam("page") Long page,
                                              @RequestParam("keyword") String keyword) {
        log.info("正在获取关注该问题的用户列表，用户：" + id + " 问答编号：" + number + " 页面数据量：" + cnt + " 当前页面：" + page + " 关键词：" + keyword);
        return ResultUtils.getResult(qaService.getFollowQAList(id, number, keyword, page, cnt),"success");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "问答编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "取消关注问答",notes = "repeatWrong：已经取消关注了 existWrong：圈子不存在 success：成功")
    @DeleteMapping("/acg/followQA")
    public Result<JSONObject> disFollowQA(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在关注问答，用户：" + id + " 问答编号：" + number);
        return ResultUtils.getResult(new JSONObject(), qaService.disFollowQA(id,number));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "回答编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "点赞回答",notes = "repeatWrong：已经点赞了 existWrong：回答不存在 success：成功")
    @PostMapping("/acg/likeAnswer")
    public Result<JSONObject> likeAnswer(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在点赞回答，用户：" + id + " 回答编号：" + number);
        return ResultUtils.getResult(new JSONObject(), qaService.likeAnswer(id,number));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "回答编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "取消点赞问答",notes = "repeatWrong：已经取消了 existWrong：回答不存在 success：成功")
    @DeleteMapping("/acg/dislikeAnswer")
    public Result<JSONObject> dislikeAnswer(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在取消点赞回答，用户：" + id + " 回答编号：" + number);
        return ResultUtils.getResult(new JSONObject(), qaService.dislikeAnswer(id,number));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "评论编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "点赞回答下面的评论",notes = "repeatWrong：已经点赞了 existWrong：评论不存在 success：成功")
    @PostMapping("/acg/likeComment")
    public Result<JSONObject> likeComment(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在点赞评论，用户：" + id + " 评论编号：" + number);
        return ResultUtils.getResult(new JSONObject(), qaService.likeComment(id,number));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "回答编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "取消点赞回答下面的评论",notes = "repeatWrong：已经取消了 existWrong：评论不存在 success：成功")
    @DeleteMapping("/acg/dislikeComment")
    public Result<JSONObject> dislikeComment(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在取消点赞评论，用户：" + id + " 评论编号：" + number);
        return ResultUtils.getResult(new JSONObject(), qaService.dislikeComment(id,number));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "问答编号",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "获取问答头部信息",notes = "existWrong：问答不存在 success：成功 返回data QATopic（number：问答编号 title：题目 description：问题内容 followCounts：关注人数 answerCounts：回答数 photo：图片（列表） label：标签（列表））")
    @GetMapping("/acg/qaTopic")
    public Result<JSONObject> getQaTopic(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在获取问答头部信息，用户id：" + id + " 问答编号：" + number);
        JSONObject jsonObject = qaService.getQATopic(id, number);
        if(jsonObject == null){
            return ResultUtils.getResult(new JSONObject(),"existWrong");
        }
        return ResultUtils.getResult(jsonObject,"success");
    }


    //@PostMapping("/acg/")


}