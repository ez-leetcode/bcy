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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "用户个人信息管理类")
@Slf4j
@RestController("/user")
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    @Autowired
    private TimeoutService timeoutService;

    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户帮助服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "photo",value = "头像文件",required = true,dataType = "file",paramType = "query")
    })
    @ApiOperation(value = "用户头像上传",notes = "fileWrong：文件为空 typeWrong：上传格式错误 success：成功（就不返回url了，会自动替换头像）")
    @PostMapping("/photoUpload")
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
            @ApiImplicitParam(name = "birthday",value = "生日（请传yyyy-mm-dd格式）",dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "用户修改基本个人信息",notes = "existWrong：账号不存在或已被冻结 success：成功")
    @PatchMapping("/personalInfo")
    public Result<JSONObject> changeInfo(@RequestParam("id") Long id,
                                         @RequestParam(value = "sex",required = false) String sex,
                                         @RequestParam(value = "description",required = false) String description,
                                         @RequestParam(value = "username",required = false) String username,
                                         @RequestParam(value = "province",required = false) String province,
                                         @RequestParam(value = "city",required = false) String city,
                                         @RequestParam(value = "birthday",required = false) String birthday){
        log.info("用户正在修改基本个人信息，用户：" + id + " 昵称：" + username + " 性别：" + sex + " 自我介绍：" + description + " 省：" + province + " 市：" + city + " 生日：" + birthday);
        return ResultUtils.getResult(new JSONObject(),personalService.changeInfo(id, sex, description, username, province, city, birthday));
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "query")
    })
    @ApiOperation(value = "修改个人设置",notes = "existWrong：账号不存在或已被冻结 success：成功")
    @PatchMapping("/personalSetting")
    public Result<JSONObject> changeSetting(@RequestParam("id") Long id){
        return null;
    }

}