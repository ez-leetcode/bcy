package com.bcy.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.community.service.MessageService;
import com.bcy.community.service.TimeoutService;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "个人消息")
@Slf4j
@RestController
//默认服务降级处理
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private TimeoutService timeoutService;


    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户问答服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取我的消息盒子中@我的列表",notes = "success：成功  返回data atList：（id：用户id username：昵称 photo：头像 description：内容" +
            " type：@类型（用来跳转）number：目标的cos或问答编号 info：右边展示的原内容 isRead：是否已读（0：未读 1：已读） createTime：@时间）")
    @GetMapping("/community/atList")
    public Result<JSONObject> getAtMessage(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                           @RequestParam("page") Long page){
        log.info("正在获取消息盒子中At我的列表，用户：" + id + " 页面数据量：" + cnt + " 当前页面：" + page);
        return ResultUtils.getResult(messageService.getAtMessageList(id, cnt, page),"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取我的消息盒子中点赞列表",notes = "success：成功 返回data likeList：（id：用户id username：昵称 photo：头像 type：类型（点赞类型）" +
            " number：被点赞的评论或者cos编号 info：右边展示原内容 isRead：是否已读 createTime：点赞时间）")
    @GetMapping("/community/likeList")
    public Result<JSONObject> getLikeMessage(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                             @RequestParam("page") Long page){
        log.info("正在获取消息盒子中点赞我的列表，用户：" + id + " 页面数据量：" + cnt + " 当前页面：" + page);
        return ResultUtils.getResult(messageService.getLikeMessageList(id, cnt, page),"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取我的消息盒子中收到评论列表",notes = "success：成功 commentList：（id：用户id username：昵称 photo：头像 description：内容 " +
            " type：评论类型 number：评论编号 info：右边展示原内容 isRead:是否已读 createTime：评论时间）")
    @GetMapping("/community/commentList")
    public Result<JSONObject> getCommentList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                             @RequestParam("page") Long page){
        log.info("正在获取消息盒子中评论我的列表，用户：" + id + " 页面数据量：" + cnt + " 当前页面：" + page);
        return ResultUtils.getResult(messageService.getCommentMessageList(id, cnt, page),"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "type",value = "类型（1：已读全部@ 2：已读全部评论 3：已读全部收到的赞 4：已读全部的消息）",required = true,dataType = "int",paramType = "query")
    })
    @ApiOperation(value = "将消息盒子中的某个内容全部设为已读",notes = "success：成功")
    @PostMapping("/community/allRead")
    public Result<JSONObject> allRead(@RequestParam("id") Long id,@RequestParam("type") Integer type){
        log.info("正在将消息盒子中某个内容全部设为已读，用户：" + id + " 类型：" + type);
        return ResultUtils.getResult(new JSONObject(),messageService.allRead(id, type));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取消息中心下的各个未读数",notes = "success：成功 返回data userNoReadCounts（id：用户id atCounts：未读的@数 commentCounts：未读评论数 likeCounts：未读点赞数 messageCounts：未读私聊消息数）")
    @GetMapping("/community/allCounts")
    public Result<JSONObject> getAllCounts(@RequestParam("id") Long id){
        log.info("正在获取消息中心下各个未读数，用户id：" + id);
        return ResultUtils.getResult(messageService.getAllCounts(id),"success");
    }

}