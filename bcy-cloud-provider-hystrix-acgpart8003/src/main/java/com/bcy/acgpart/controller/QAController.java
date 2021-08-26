package com.bcy.acgpart.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.acgpart.service.QAService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "photo",required = true,dataType = "file",paramType = "query")
    })
    @ApiOperation(value = "问答图片上传",notes = "fileWrong：文件为空 typeWrong：文件类型错误 success：成功（返回json带url）")
    @PostMapping("/acg/photoUpload")
    public Result<JSONObject> photoUpload(@RequestParam("photo") MultipartFile file){
        log.info("正在上传问答图片");
        String url = qaService.photoUpload(file);
        if(url.length() < 12){
            return ResultUtils.getResult(new JSONObject(),url);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url",url);
        return ResultUtils.getResult(jsonObject,"success");
    }

    @HystrixCommand
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

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "问答编号",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "keyword",value = "关键词",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "获取关注该问题的用户列表",notes = "success：成功 返回data：followQAList（id：用户id username：用户昵称 fansCounts：粉丝数 photo：头像url）")
    @GetMapping("/acg/followQAList")
    public Result<JSONObject> getFollowQAList(@RequestParam(value = "id",required = false) Long id,@RequestParam("number") Long number,
                                              @RequestParam("cnt") Long cnt,@RequestParam("page") Long page,
                                              @RequestParam("keyword") String keyword) {
        log.info("正在获取关注该问题的用户列表，用户：" + id + " 问答编号：" + number + " 页面数据量：" + cnt + " 当前页面：" + page + " 关键词：" + keyword);
        return ResultUtils.getResult(qaService.getFollowQAList(id, number, keyword, page, cnt),"success");
    }

    @HystrixCommand
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

    @HystrixCommand
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

    @HystrixCommand
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

    @HystrixCommand
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

    @HystrixCommand
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

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "问答编号",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "获取问答头部信息",notes = "existWrong：问答不存在 success：成功 返回data QATopic（number：问答编号 title：题目 description：问题内容 followCounts：关注人数 answerCounts：回答数 photo：图片（列表） label：标签（列表））")
    @GetMapping("/acg/qaTopic")
    public Result<JSONObject> getQaTopic(@RequestParam(value = "id",required = false) Long id,@RequestParam("number") Long number){
        log.info("正在获取问答头部信息，用户id：" + id + " 问答编号：" + number);
        JSONObject jsonObject = qaService.getQATopic(id, number);
        if(jsonObject == null){
            return ResultUtils.getResult(new JSONObject(),"existWrong");
        }
        return ResultUtils.getResult(jsonObject,"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "问题编号",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "type",value = "排序类型（1：最热 2：最新）",required = true,dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取回答列表（点赞数 收藏数 评论数以及回答下评论见另外一个接口）",notes = "success：成功 返回data answerList（number：评论编号 id：用户id username：昵称 description：主要内容" +
            "photo：用户头像 answerPhoto：回答的图片（list） createTime：回答时间）")
    @GetMapping("/acg/answerList")
    public Result<JSONObject> getAnswerList(@RequestParam(value = "id",required = false) Long id,
                                            @RequestParam("number") Long number,@RequestParam("type") Integer type,
                                            @RequestParam("cnt") Long cnt,@RequestParam("page") Long page){
        log.info("正在获取回答列表，用户：" + id + "问题编号：" + number + " 排序类型：" + type + " 页面数据量：" + cnt + " 当前页面：" + page);
        return ResultUtils.getResult(qaService.getQAAnswerList(id, number, type, cnt, page),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "answerNumber",value = "回答编号",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "type",value = "排序方式 1：热度 2：时间",required = true,dataType = "int",paramType = "query")
    })
    @ApiOperation(value = "获取问答下回答的评论列表（在问答主页面也用这个接口获取下面的回复数据，cnt请填3 page请填1 type请填1）",notes = "success：成功 返回answerCommentList（number：评论编号 id:：评论者id username：评论者昵称 photo：评论者头像 description：评论内容 createTime：评论时间）")
    @GetMapping("/acg/answerCommentList")
    public Result<JSONObject> getAnswerCommentList(@RequestParam(value = "id",required = false) Long id,
                                                   @RequestParam("answerNumber") Long answerNumber,
                                                   @RequestParam("cnt") Long cnt ,@RequestParam("page") Long page,
                                                   @RequestParam("type") Integer type){
        log.info("正在获取问答下回答的评论列表，用户：" + id + " 回答编号：" + answerNumber + " 页面数据量：" + cnt + " 当前页面：" + page + " 排序类型：" + type);
        return ResultUtils.getResult(qaService.getAnswerCommentList(id, answerNumber, page, cnt, type),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "问答评论编号",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "type",value = "排序顺序",required = true,dataType = "int",paramType = "query")
    })
    @ApiOperation(value = "获取问答下回答的评论的评论列表（在回答主页面也用这个接口获取下面的数据 cnt请填3 page请填1 type请填1）",notes = "success：成功 返回data answerCommentCommentList（number：评论编号 fromId:：评论者id" +
            " fromUsername：评论者昵称 fromPhoto：评论者头像 description：评论内容 toId：被回复者id（没有回复别人为空） toUsername：被回复者昵称 createTime：评论时间）")
    @GetMapping("/acg/answerCommentCommentList")
    public Result<JSONObject> getAnswerCommentCommentList(@RequestParam(value = "id",required = false) Long id,
                                                          @RequestParam("number") Long number,
                                                          @RequestParam("cnt") Long cnt,@RequestParam("page") Long page,
                                                          @RequestParam("type") Integer type){
        log.info("正在获取问答下回答的评论的评论列表，用户：" + id + " 问答评论编号：" + number + " 页面数据量：" + cnt + " 当前页面：" + page + " 排序顺序：" + type);
        return ResultUtils.getResult(qaService.getAnswerCommentCommentList(id, number, page, cnt, type),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "title",value = "问答标题",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "description",value = "问答内容",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "photo",value = "图片列表",required = true,allowMultiple = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "label",value = "标签列表",required = true,allowMultiple = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "生成问答",notes = "dirtyWrong：问答标题或内容有敏感内容（会推送） repeatWrong：24小时内超过15次 success：成功")
    @PostMapping("/acg/QA")
    public Result<JSONObject> generateQA(@RequestParam("id") Long id, @RequestParam("title") String title,
                                         @RequestParam("description") String description, @RequestParam("photo")List<String> photo,
                                         @RequestParam("label") List<String> label){
        log.info("正在生成问答，用户：" + id + " 标题：" + title + " 描述：" + description + " 图片：" + photo.toString());
        return ResultUtils.getResult(new JSONObject(), qaService.generateQA(id, title, description, photo, label));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "description",value = "回答内容",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "photo",value = "回答图片（list）",required = true,allowMultiple = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "问题编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "回答问题",notes = "existWrong：问题不存在 dirtyWrong：有脏话 success：成功")
    @PostMapping("/acg/addAnswer")
    public Result<JSONObject> addAnswer(@RequestParam("id") Long id,@RequestParam("description") String description,
                                        @RequestParam("photo") List<String> photo,@RequestParam("number") Long number){
        log.info("正在回答问题，用户：" + id + " 回答内容：" + description + " 问题编号：" + number);
        return ResultUtils.getResult(new JSONObject(),qaService.addAnswer(id,number,description,photo));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "answerNumber",value = "回答编号",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "description",value = "评论内容",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "fatherNumber",value = "父评论",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "toId",value = "回复id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "replyNumber",value = "回复评论编号",dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "添加问题评论",notes = "success：成功")
    @PostMapping("/acg/answerComment")
    public Result<JSONObject> addAnswerComment(@RequestParam("id") Long id,@RequestParam("description") String description,
                                               @RequestParam("answerNumber") Long answerNumber,@RequestParam(value = "fatherNumber",required = false) Long fatherNumber,
                                               @RequestParam(value = "toId",required = false) Long toId,
                                               @RequestParam(value = "replyNumber",required = false) Long replyNumber){
        log.info("正在添加问题评论，用户：" + id + " 评论内容：" + description + " 问答编号：" + answerNumber + " 父评论：" + fatherNumber + " 回复id：" + toId + " 回复评论编号：" + replyNumber);
        return ResultUtils.getResult(new JSONObject(),qaService.addAnswerComment(id,answerNumber,description,fatherNumber,replyNumber,toId));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "numbers",value = "回答编号",required = true,allowMultiple = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取问答回答的点赞 评论数",notes = "success：成功 返回qaAnswerCountsList：（number：问答回答编号 likeCounts：点赞数 commentCounts：评论数）")
    @GetMapping("/acg/qaAnswerCountsList")
    public Result<JSONObject> getQaAnswerCountsList(@RequestParam(value = "id",required = false) Long id,
                                                    @RequestParam("numbers") List<Long> numbers){
        log.info("正在获取问答回答的点赞评论数，用户：" + id + " 回答编号：" + numbers.toString());
        return ResultUtils.getResult(qaService.getQAAnswerCountsList(id, numbers),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "numbers",value = "评论编号",required = true,allowMultiple = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取问答评论的点赞 评论数",notes = "success：成功 返回qaCommentCountList：（number：问答评论编号 likeCounts：点赞数 commentCounts：评论数）")
    @GetMapping("/acg/qaCommentCountsList")
    public Result<JSONObject> getCommentCountsList(@RequestParam(value = "id",required = false) Long id,
                                                   @RequestParam("numbers") List<Long> numbers){
        log.info("正在获取评论的点赞评论数，用户：" + id + " 评论编号：" + numbers.toString());
        return ResultUtils.getResult(qaService.getQACommentCountsList(id, numbers),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "numbers",value = "问答编号",required = true,allowMultiple = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取问答的关注数 回答数",notes = "success：成功 返回qaCountsList：（number：问答编号 followCounts：关注数 answerCounts：回答数）")
    @GetMapping("/acg/qaCountsList")
    public Result<JSONObject> getQaCountsList(@RequestParam(value = "id",required = false) Long id,
                                              @RequestParam("numbers") List<Long> numbers){
        log.info("正在获取问答关注回答数，用户：" + id + " 问答编号：" + numbers.toString());
        return ResultUtils.getResult(qaService.getQACountsList(id, numbers),"success");
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
        @ApiImplicitParam(name = "numbers",value = "问答编号",required = true,allowMultiple = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取问答关注情况",notes = "success：成功 返回qaJudgeList：（number：问答编号 isFollow：1关注 0未关注）")
    @GetMapping("/acg/judgeQa")
    public Result<JSONObject> judgeQaFollow(@RequestParam("id") Long id,@RequestParam("numbers") List<Long> numbers){
        log.info("正在获取问答关注情况，用户：" + id);
        return ResultUtils.getResult(qaService.qaJudgeList(id, numbers),"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "numbers",value = "回答编号",required = true,allowMultiple = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取问答回答点赞情况",notes = "success：成功 返回qaAnswerLikeList：（number：回答编号 isLike：1：已点赞 0未点赞）")
    @GetMapping("/acg/judgeQaAnswer")
    public Result<JSONObject> judgeQaAnswer(@RequestParam("id") Long id,@RequestParam("numbers") List<Long> numbers){
        log.info("正在获取问答回答点赞情况，用户：" + id);
        return ResultUtils.getResult(qaService.qaAnswerJudgeList(id, numbers),"success");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "numbers",value = "评论编号",required = true,allowMultiple = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取问答评论点赞情况",notes = "success：成功 返回qaCommentLikeList：（number：评论编号 isLike：1：已点赞 0未点赞）")
    @GetMapping("/acg/judgeQaComment")
    public Result<JSONObject> judgeQaComment(@RequestParam("id") Long id,@RequestParam("numbers") List<Long> numbers){
        log.info("正在获取问答评论点赞情况，用户：" + id);
        return ResultUtils.getResult(qaService.qaCommentJudgeList(id, numbers),"success");
    }

}