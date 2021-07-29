package com.bcy.userpart.controller;


import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;
import com.bcy.userpart.service.SmsService;
import com.bcy.userpart.service.TimeoutService;
import com.bcy.userpart.utils.RedisUtils;
import com.bcy.utils.ResultUtils;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Api(tags = "短信验证码类")
@Slf4j
@RestController("/user")
//默认服务降级处理
@DefaultProperties(defaultFallback = "timeoutHandler")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private TimeoutService timeoutService;

    @Autowired
    private RedisUtils redisUtils;


    //超时或内部出错调用方法，进行服务降级
    public Result<JSONObject> timeoutHandler(){
        log.error("用户短信验证码服务超时或发生错误");
        return timeoutService.timeoutHandler();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone",value = "电话",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "type",value = "哪种验证码（1.注册 2.修改密码 3.找回密码 4.登录）",required = true,dataType = "int",paramType = "query")
    })
    @ApiOperation(value = "发送短信验证码",notes = "repeatWrong：获取验证码次数过多，existWrong：手机号不存在（验证码发送错误） success：成功")
    @PostMapping("/code")
    public Result<JSONObject> sendCode(@RequestParam("phone") String phone, @RequestParam("type") Integer type){
        log.info("正在发送短信验证码，电话：" + phone + " 类型：" + type);
        String status = smsService.judgeCode(phone,type);
        if(!status.equals("success")){
            return ResultUtils.getResult(new JSONObject(),status);
        }
        Random random = new Random();
        int yzm = random.nextInt(999999);
        String code = Integer.toString(yzm);
        boolean isSend = smsService.sendSms(phone,code,type);
        if(isSend){
            //成功发送验证码
            redisUtils.saveByMinutesTime(type + "_" + phone,code,15);
            String sendCounts = redisUtils.getValue("sendCode_" + phone);
            int cnt = 0;
            if(sendCounts != null){
                cnt = Integer.parseInt(sendCounts);
            }
            cnt ++;
            //保存近期发送时间
            redisUtils.saveByHoursTime("sendCode_",String.valueOf(cnt),2);
            return ResultUtils.getResult(new JSONObject(),"success");
        }
        return ResultUtils.getResult(new JSONObject(),"existWrong");
    }

}