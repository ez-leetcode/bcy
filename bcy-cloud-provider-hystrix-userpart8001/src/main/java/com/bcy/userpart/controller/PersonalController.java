package com.bcy.userpart.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;
import com.bcy.userpart.service.PersonalService;
import com.bcy.userpart.service.TimeoutService;
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

import java.util.List;

@Api(tags = "用户个人信息管理类")
@Slf4j
@RestController
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    @Autowired
    private TimeoutService timeoutService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户个人信息管理服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "photo",value = "头像文件",required = true,dataType = "file",paramType = "query")
    })
    @ApiOperation(value = "用户头像上传P",notes = "fileWrong：文件为空 typeWrong：上传格式错误 success：成功（就不返回url了，会自动替换头像）")
    @PostMapping("/user/photoUpload")
    public Result<JSONObject> userPhotoUpload(@RequestParam("photo")MultipartFile file,Long id){
        log.info("正在上传头像，用户：" + id);
        String url = personalService.userPhotoUpload(file,id);
        if(url.length() < 12){
            //没上传成功返回了错误的信息
            return ResultUtils.getResult(new JSONObject(),url);
        }
        return ResultUtils.getResult(new JSONObject(),"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "sex",value = "性别",dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "description",value = "自我介绍",dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "username",value = "昵称",dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "province",value = "省",dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "city",value = "市",dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "birthday",value = "生日（请传yyyy-MM-dd格式）",dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "用户修改基本个人信息（修改头像在上传头像那个接口会自动修改）P",notes = "existWrong：账号不存在或已被冻结 success：成功")
    @PatchMapping("/user/personalInfo")
    public Result<JSONObject> changeInfo(@RequestParam("id") Long id,
                                         @RequestParam(value = "sex",required = false) String sex,
                                         @RequestParam(value = "description",required = false) String description,
                                         @RequestParam(value = "username",required = false) String username,
                                         @RequestParam(value = "province",required = false) String province,
                                         @RequestParam(value = "city",required = false) String city,
                                         @RequestParam(value = "birthday",required = false) String birthday){
        log.info("用户正在修改基本个人信息，用户：" + id + " 昵称：" + username + " 性别：" + sex + " 自我介绍：" + description + " 省：" + province + " 市：" + city + " 生日：" + birthday);
        return ResultUtils.getResult(new JSONObject(),personalService.changeInfo(id,username,sex,description,province,city,birthday));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "phone",value = "手机号",dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "获取用户基本个人信息（个人主页）P",notes = "existWrong：账号不存在或已被冻结 success：成功 返回data personalInfo（id：用户id username：昵称 sex：用户性别 photo：头像url description：用户简介 province：省 city：市 birthday：生日）")
    @GetMapping("/user/personalInfo")
    public Result<JSONObject> getPersonalInfo(@RequestParam(value = "id",required = false) Long id,@RequestParam(value = "phone",required = false) String phone){
        log.info("正在获取用户基本个人信息，用户：" + id + " 手机号：" + phone);
        JSONObject jsonObject = personalService.getPersonalInfo(id,phone);
        if(jsonObject == null){
            return ResultUtils.getResult(new JSONObject(),"existWrong");
        }
        return ResultUtils.getResult(jsonObject,"success");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "pushComment",value = "推送评论",dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "pushLike",value = "推送点赞",dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "pushFans",value = "推送粉丝",dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "pushSystem",value = "推送系统通知",dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "pushInfo",value = "推送聊天",dataType = "int",paramType = "query"),
    })
    @ApiOperation(value = "修改个人设置P",notes = "existWrong：账号不存在或已被冻结 success：成功")
    @PatchMapping("/user/personalSetting")
    public Result<JSONObject> changeSetting(@RequestParam("id") Long id,
                                            @RequestParam(value = "pushComment",required = false) Integer pushComment,
                                            @RequestParam(value = "pushLike",required = false) Integer pushLike,
                                            @RequestParam(value = "pushFans",required = false) Integer pushFans,
                                            @RequestParam(value = "pushSystem",required = false) Integer pushSystem,
                                            @RequestParam(value = "pushInfo",required = false) Integer pushInfo){
        log.info("正在修改用户个人设置，用户：" + id + " 推送评论：" + pushComment + " 推送点赞：" + pushLike + " 推送粉丝：" + pushFans + " 推送系统通知：" + pushSystem + " 推送聊天：" + pushInfo);
        return ResultUtils.getResult(new JSONObject(),personalService.changeSetting(id,pushComment,pushLike,pushFans,pushSystem,pushInfo));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取用户个人设置P",notes = "existWrong：用户不存在或已被冻结 success：成功 返回data：personalSetting（id：用户id pushComment：推送评论 pushLike：推送点赞 pushFans：推送粉丝 pushSystem：推送系统通知 pushInfo：推送聊天）")
    @GetMapping("/user/personalSetting")
    public Result<JSONObject> getPersonalSetting(@RequestParam("id") Long id){
        log.info("正在获取用户个人设置，用户id：" + id);
        JSONObject jsonObject = personalService.getPersonalSetting(id);
        if(jsonObject == null){
            return ResultUtils.getResult(new JSONObject(),"existWrong");
        }
        return ResultUtils.getResult(jsonObject,"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id（这里不用id做参数是因为过过滤器会检测）",required = true,allowMultiple = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "获取用户的粉丝数 关注数 动态数（统一接口，所有人的信息都可以在这里获取）P",notes = "success：成功 返回userCountsList（id：用户id fansCounts：粉丝数 followCounts：关注数 momentCounts：动态数）")
    @GetMapping("/user/userCounts")
    public Result<JSONObject> getUserCounts(@RequestParam("userId")List<Long> userId){
        log.info("正在获取用户粉丝数 关注数 动态数，用户：" + userId.toString());
        return ResultUtils.getResult(personalService.getUserCounts(userId),"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "判断是否为新用户",notes = "existWrong：用户不存在 success：成功 返回data： isNew：1是新人跳转填写用户信息 0不是新人")
    @PostMapping("/user/judgeNew")
    public Result<JSONObject> judgeNewUser(@RequestParam("id") Long id){
        log.info("正在判断是否为新用户，用户：" + id);
        JSONObject jsonObject = personalService.judgeNew(id);
        if(jsonObject == null){
            return ResultUtils.getResult(new JSONObject(),"existWrong");
        }
        return ResultUtils.getResult(jsonObject,"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "password",value = "新密码",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "新用户设置密码",notes = "existWrong：用户不存在 success：成功")
    @PostMapping("/user/setPassword")
    public Result<JSONObject> setPassword(@RequestParam("id") Long id,@RequestParam("password") String password){
        log.info("正在设置新用户密码，用户：" + id + " 新密码：" + password);
        return ResultUtils.getResult(new JSONObject(),personalService.setPassword(id, password));
    }

}