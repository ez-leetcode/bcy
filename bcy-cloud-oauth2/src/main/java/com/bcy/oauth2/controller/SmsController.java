package com.bcy.oauth2.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcy.oauth2.service.SmsService;
import com.bcy.oauth2.utils.RedisUtils;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
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


@Api(tags = "用户验证码管理类")
@Slf4j
@RestController
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisUtils redisUtils;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone",value = "电话",required = true,dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "type",value = "哪种验证码（1.登录（没账号会自动注册） 2.修改密码 3.找回密码 ）",required = true,dataType = "int",paramType = "query")
    })
    @ApiOperation(value = "发送短信验证码（15分钟有效）",notes = "repeatWrong：获取验证码次数过多，existWrong：手机号不存在（验证码发送错误） success：成功")
    @PostMapping("/oauth/code")
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
            redisUtils.saveByHoursTime("sendCode_" + phone,String.valueOf(cnt),2);
            return ResultUtils.getResult(new JSONObject(),"success");
        }
        return ResultUtils.getResult(new JSONObject(),"existWrong");
    }

}
