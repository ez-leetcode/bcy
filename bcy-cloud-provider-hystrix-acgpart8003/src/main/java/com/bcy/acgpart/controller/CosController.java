package com.bcy.acgpart.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.acgpart.service.CosService;
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

@RestController
@Api(tags = "cos管理类")
@Slf4j
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class CosController {

    @Autowired
    private TimeoutService timeoutService;

    @Autowired
    private CosService cosService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户问答服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "numbers",value = "讨论编号",required = true,allowMultiple = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "批量删除Cos（管理员用，如果违反规定就删除）（只有一个也扔这个接口~）",notes = "existWrong：讨论不存在 success：成功")
    @DeleteMapping("/acg/cos")
    public Result<JSONObject> deleteCos(@RequestParam("numbers")List<Long> numbers){
        log.info("正在批量删除讨论" + numbers.toString());
        return ResultUtils.getResult(new JSONObject(), cosService.deleteCos(numbers));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "cos编号（list）",allowMultiple = true,required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取cos的 点赞 评论 收藏数",notes = "success：成功 返回data cosCountsList（number：cos编号 commentCounts：评论数 likeCounts：点赞数 favorCounts：收藏数 shareCounts：分享数")
    @GetMapping("/acg/cosCountsList")
    public Result<JSONObject> getCosCountsList(@RequestParam(value = "id",required = false) Long id,@RequestParam("number") List<Long> number){
        log.info("正在获取cos的计数信息，用户：" + id + " cos编号：" + number.toString());
        return ResultUtils.getResult(cosService.getCosCountsList(id,number),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "评论编号（list）",required = true,dataType = "Long",allowMultiple = true,paramType = "query")
    })
    @ApiOperation(value = "获取cos下面评论的点赞数和评论数",notes = "success：成功 返回data cosCommentCountsList（number：评论编号 likeCounts：点赞数 commentCounts：评论数）")
    @GetMapping("/acg/cosCommentCountsList")
    public Result<JSONObject> getCosCommentCountsList(@RequestParam(value = "id",required = false) Long id,
                                                      @RequestParam("number") List<Long> number){
        log.info("正在获取cos下面评论的点赞数和评论数，用户：" + id + " 评论编号：" + number.toString());
        return ResultUtils.getResult(cosService.getCosCommentCountsList(id, number),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "description",value = "描述",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "photo",value = "图片列表（list）",required = true,allowMultiple = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "label",value = "标签（list）",required = true,allowMultiple = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "permission",value = "权限（1：所有人可见 2：粉丝和自己可见 3：仅自己可见）",required = true,dataType = "int",paramType = "query")
    })
    @ApiOperation(value = "发布cos",notes = "dirtyWrong：描述里有敏感词汇（会推送） repeatWrong：24小时内发布cos超过15次 不让发了 success：成功")
    @PostMapping("/acg/cos")
    public Result<JSONObject> createCos(@RequestParam("id") Long id, @RequestParam("description") String description,
                                        @RequestParam("photo") List<String> photo,@RequestParam("label") List<String> label,
                                        @RequestParam("permission") Integer permission){
        log.info("正在发起讨论，用户：" + id + " 描述：" + description + " 图片列表：" + photo.toString() + " 标签列表：" + label.toString() + " 权限：" + permission);
        return ResultUtils.getResult(new JSONObject(),cosService.generateCos(id, description, photo, label,permission));
    }


    //es同步要考虑
    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "cos编号",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "description",value = "描述",dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "cosPhoto",value = "新的图片列表（list）",allowMultiple = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "permission",value = "权限",dataType = "int",paramType = "query")
    })
    @ApiOperation(value = "修改cos内容（没改的参数不要带，或者可以带空，图片要注意下带空就是没图）",notes = "userWrong：用户不存在在 existWrong：cos不存在 success：成功")
    @PutMapping("/acg/cos")
    public Result<JSONObject> patchCos(@RequestParam("id") Long id,@RequestParam("number") Long number,
                                       @RequestParam(value = "description",required = false) String description,
                                       @RequestParam(value = "cosPhoto",required = false) List<String> cosPhoto,
                                       @RequestParam(value = "permission",required = false) Integer permission){
        log.info("正在修改cos内容，用户：" + id  + " cos编号：" + number + " 描述：" + description + " 图片列表：" + cosPhoto.toString() + " 权限：" + permission);
        return ResultUtils.getResult(new JSONObject(),cosService.patchCos(id, number, description, cosPhoto, permission));
    }

    //会存入历史记录
    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "cos编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取cos页面上半部分内容（获取底下评论在另外一个接口）",notes = "existWrong：cos不存在 success：成功 返回data cos（number：cos编号 id：用户id username：发布者昵称 photo：发布者头像 fansCounts：粉丝数" +
            "description：内容 cosPhoto：图片（list） label：标签（list） createTime：发布时间 ）")
    @GetMapping("/acg/cos")
    public Result<JSONObject> getCosTopic(@RequestParam(value = "id",required = false) Long id,@RequestParam("number") Long number){
        log.info("正在获取cos上半部分内容，用户：" + id + " cos编号：" + number);
        JSONObject jsonObject = cosService.getCosTopic(id,number);
        if(jsonObject == null){
            return ResultUtils.getResult(new JSONObject(),"existWrong");
        }
        return ResultUtils.getResult(jsonObject,"success");
    }

    //获取下层的评论
    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "cos编号",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "type",value = "排序方式（0：按时间 1：按热度）",required = true,dataType = "int",paramType = "query")
    })
    @ApiOperation(value = "获取cos页面下面的评论内容",notes = "existWrong：cos不存在 success：成功 cosCommentList （number：评论编号 id：用户id username：用户昵称 photo：头像 description：评论内容 createTime：评论时间）")
    @GetMapping("/acg/cosComment")
    public Result<JSONObject> getCosComment(@RequestParam(value = "id",required = false) Long id,
                                            @RequestParam("number") Long number, @RequestParam("page") Long page,
                                            @RequestParam("cnt") Long cnt,@RequestParam("type") Integer type){
        log.info("正在获取cos页面下面的内容，用户：" + id + "cos编号：" + number + " 当前页面：" + page + " 页面数据量：" + cnt + " 排序方式：" + type);
        JSONObject jsonObject = cosService.getCosComment(id, number, page, cnt, type);
        if(jsonObject == null){
            return ResultUtils.getResult(new JSONObject(),"existWrong");
        }
        return ResultUtils.getResult(jsonObject,"success");
    }

    //推送待完成
    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cosNumber",value = "cos的编号",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "description",value = "评论内容",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "fatherNumber",value = "父评论编号（没有父评论填0或不带这个参数）",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "toId",value = "回复id（没有回复别人填0或不带这个参数）",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "replyNumber",value = "回复评论的编号（没有回复评论填0或不带这个参数）",dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "发表cos下面的评论",notes = "success：成功")
    @PostMapping("/acg/cosComment")
    public Result<JSONObject> addCosComment(@RequestParam("id") Long id,@RequestParam("cosNumber") Long cosNumber,
                                            @RequestParam("description") String description,
                                            @RequestParam(value = "fatherNumber",required = false) Long fatherNumber,
                                            @RequestParam(value = "toId",required = false) Long toId,
                                            @RequestParam(value = "replyNumber",required = false) Long replyNumber){
        log.info("正在发表cos下面的评论，用户：" + id + " cos编号：" + cosNumber + " 内容：" + description
                + " 父评论编号：" + fatherNumber + " 回复id：" + toId + " 回复评论编号：" + replyNumber);
        return ResultUtils.getResult(new JSONObject(), cosService.addComment(id, cosNumber, description, fatherNumber, toId, replyNumber));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "评论编号",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "type",value = "排序方式 1：热度 2：时间",required = true,dataType = "int",paramType = "query")
    })
    @ApiOperation(value = "获取cos下面评论的评论列表（在cos页面也用这个接口获取下面的回复数据，cnt请填3 page请填1 type请填1）",notes = "success：成功" +
            "返回data commentCommentList：（number：评论编号 fromId:：评论者id fromUsername：评论者昵称 fromPhoto：评论者头像 description：评论内容 toId：被回复者id（没有回复别人为空） toUsername：被回复者昵称 createTime：评论时间）")
    @GetMapping("/acg/cosCommentComment")
    public Result<JSONObject> getCosCommentComment(@RequestParam(value = "id",required = false) Long id,
                                                   @RequestParam("number") Long number,@RequestParam("cnt") Long cnt,
                                                   @RequestParam("page") Long page,@RequestParam("type") Integer type){
        log.info("正在获取cos下面的评论列表，用户：" + id + " 评论编号：" + number + " 页面数据量：" + cnt + " 当前页面：" + page + " 排序类型：" + type);
        JSONObject jsonObject = cosService.getCosCommentList(id, number, cnt, page, type);
        if(jsonObject == null){
            return ResultUtils.getResult(new JSONObject(),"existWrong");
        }
        return ResultUtils.getResult(jsonObject,"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "评论编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "点赞cos下的评论",notes = "existWrong：评论不存在 repeatWrong：重复点赞 success：成功")
    @PostMapping("/acg/likeCosComment")
    public Result<JSONObject> likeCosComment(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在点赞cos下的评论，用户：" + id + " 评论编号：" + number);
        return ResultUtils.getResult(new JSONObject(),cosService.likeCosComment(id, number));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "评论编号",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "取消点赞cos下的评论",notes = "existWrong：评论不存在 repeatWrong：重复取消点赞 success：成功")
    @DeleteMapping("/acg/likeCosComment")
    public Result<JSONObject> dislikeCosComment(@RequestParam("id") Long id,@RequestParam("number") Long number){
        log.info("正在取消点赞cos下的评论，用户：" + id + " 评论编号：" + number);
        return ResultUtils.getResult(new JSONObject(),cosService.dislikeCosComment(id, number));
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "photo",required = true,dataType = "file",paramType = "query")
    })
    @ApiOperation(value = "cos图片上传",notes = "fileWrong：文件为空 typeWrong：文件类型错误 success：成功（返回json带url）")
    @PostMapping("/acg/cosPhotoUpload")
    public Result<JSONObject> photoUpload(@RequestParam("photo") MultipartFile file){
        log.info("正在上传cos图片");
        String url = cosService.cosPhotoUpload(file);
        if(url.length() < 12){
            return ResultUtils.getResult(new JSONObject(),url);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url",url);
        return ResultUtils.getResult(jsonObject,"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time",value = "日期（请带yyyy-MM-dd格式）",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "获取日榜热门cos列表",notes = "success：成功 返回hotCosList：（cosNumber：cos编号 id：用户id username：昵称 photo：头像 cosPhoto：cos图片列表" +
            "cosLabel：cos标签 createTime：cos发布时间）")
    @GetMapping("/acg/hotDayCos")
    public Result<JSONObject> getHotDayCos(@RequestParam("time") String time){
        log.info("正在获取日榜热门cos列表，时间：" + time);
        return ResultUtils.getResult(cosService.getCosDayHotList(time),"success");
    }

    //这里week写成month了 其实是周榜
    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time",value = "日期（请带yyyy-MM-dd格式）请给周一的日期",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "获取周榜热门cos列表",notes = "success：成功 返回hotCosList：（cosNumber：cos编号 id：用户id username：昵称 photo：头像 cosPhoto：cos图片列表" +
            "cosLabel：cos标签 createTime：cos发布时间）")
    @GetMapping("/acg/hotWeekCos")
    public Result<JSONObject> getHotMonthCos(@RequestParam("time") String time){
        log.info("正在获取周榜热门cos列表，时间：" + time);
        return ResultUtils.getResult(cosService.getCosMonthHotList(time),"success");
    }

    @HystrixCommand
    @ApiOperation(value = "获取推荐cos标签",notes = "success：成功 返回data cosRecommendLabelList：label（list）：推荐标签列表 一般会有20个")
    @GetMapping("/acg/recommendList")
    public Result<JSONObject> getRecommendList(){
        log.info("正在获取推荐cos标签");
        return ResultUtils.getResult(cosService.getRecommendLabelList(),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取关注的未读数（获取关注的cos的关注列表后会清空）",notes = "success：成功 noReadCounts（未读数）")
    @GetMapping("/acg/followNoRead")
    public Result<JSONObject> getFollowNoRead(@RequestParam("id") Long id){
        log.info("正在获取关注的未读数，用户：" + id);
        return ResultUtils.getResult(cosService.getFollowNoRead(id),"success");
    }

    @HystrixCommand
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "cnt",value = "页面数据量",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页面",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取关注用户的cos列表",notes = "success：成功 返回data cosFollowList（number：cos编号 id：用户id username：昵称 photo：用户头像 cosPhoto：cos图片（list） label：标签（list） createTime：发布时间）")
    @GetMapping("/acg/followCos")
    public Result<JSONObject> getFollowList(@RequestParam("id") Long id,@RequestParam("cnt") Long cnt,
                                            @RequestParam("page") Long page){
        log.info("正在获取用户cos列表，用户：" + id + " 页面数据量：" + cnt + " 当前页面：" + page);
        return ResultUtils.getResult(cosService.getFollowList(id,page,cnt),"success");
    }

}