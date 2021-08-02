package com.bcy.oauth2.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.oauth2.service.UserService;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户登录管理类")
@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone",value = "电话",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "code",value = "验证码",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "type",value = "登录端（前端填1 安卓填2）",required = true,dataType = "int",paramType = "query")
    })
    @ApiOperation(value = "用户短信验证码登录",notes = "codeWrong：验证码错误 success：成功（会给token）")
    @PostMapping("/oauth/loginBySms")
    public Result<JSONObject> smsLogin(@RequestParam("phone") String phone,@RequestParam("code") String code,
                                       @RequestParam("type") Integer type){
        log.info("用户正在用短信验证码登录，电话：" + phone + " 验证码：" + code);
        if(type == 1){
            log.info("登录端：PC");
        }else {
            log.info("登录端：安卓");
        }
        String status = userService.loginByCode(phone,code,type);
        if(status.equals("codeWrong")){
            return ResultUtils.getResult(new JSONObject(),"codeWrong");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token",status);
        return ResultUtils.getResult(jsonObject,"success");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone",value = "用户id",required = true,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "newPassword",value = "密码",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "code",value = "验证码",required = true,dataType = "string",paramType = "query")
    })
    @ApiOperation(value = "找回密码",notes = "codeWrong：验证码错误（不存在或者错误） existWrong：账号不存在 success：成功")
    @PostMapping("/oauth/changePassword")
    public Result<JSONObject> changePassword(@RequestParam("id") Long id,@RequestParam("newPassword") String newPassword,
                                             @RequestParam("code") String code){
        log.info("正在修改密码，用户：" + id + " 新密码：" + newPassword + " 验证码：" + code);
        return ResultUtils.getResult(new JSONObject(),userService.changePassword(newPassword,newPassword,code));
    }

    @PostMapping("/oauth/test")
    public String test(){
        return "success";
    }

}