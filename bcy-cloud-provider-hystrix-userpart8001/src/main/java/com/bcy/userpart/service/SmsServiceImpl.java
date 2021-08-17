package com.bcy.userpart.service;

import com.bcy.userpart.mapper.UserLoginMapper;
import com.bcy.userpart.pojo.UserLogin;
import com.bcy.userpart.utils.RedisUtils;
import com.bcy.utils.TencentSmsUtils;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService{

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserLoginMapper userLoginMapper;

    @Override
    public String changePhone(Long id, String phone, String code) {
        String redisCode = redisUtils.getValue("5_" + phone);
        if(redisCode == null || !redisCode.equals(code)){
            log.warn("修改手机号失败，验证码不正确或不存在");
            return "codeWrong";
        }
        UserLogin userLogin = userLoginMapper.selectById(id);
        if(userLogin != null){
            //改绑手机
            userLogin.setPhone(phone);
            userLoginMapper.updateById(userLogin);
        }
        //移除验证码
        redisUtils.delete("5_" + phone);
        log.info("用户改绑手机成功");
        return "success";
    }


    @Override
    public boolean sendSms(String phone, String code, int templateCode) {
        log.info("正在发送短信验证码，电话：" + phone);
        log.info("验证码：" + code);
        log.info("模板：" + templateCode);
        try{
            Credential credential = new Credential(TencentSmsUtils.ACCESS_ID,TencentSmsUtils.ACCESS_KEY);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setConnTimeout(60);
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            //实例化sms的client对象
            SmsClient smsClient = new SmsClient(credential,"ap-guangzhou",clientProfile);
            SendSmsRequest request = new SendSmsRequest();
            request.setSmsSdkAppid("1400520404");
            request.setSign("小青龙XiaoQingL");
            request.setTemplateID(TencentSmsUtils.TEMPLATE.get(templateCode));
            //腾讯云手机号要+86表示
            String [] phoneNumber = {"+86" + phone};
            request.setPhoneNumberSet(phoneNumber);
            //模板参数，放验证码
            String [] templateParams = {code};
            request.setTemplateParamSet(templateParams);
            /* 通过 client 对象调用 SendSms 方法发起请求。注意请求方法名与请求对象是对应的
             * 返回的 res 是一个 SendSmsResponse 类的实例，与请求对象对应 */
            SendSmsResponse response = smsClient.SendSms(request);
            log.info(SendSmsResponse.toJsonString(response));
        }catch (Exception e){
            e.printStackTrace();
            log.error("发送短信出现错误");
            return false;
        }
        log.info("发送短信成功");
        return true;
    }

    @Override
    public String judgeCode(String phone, Integer type) {
        String sendCount = redisUtils.getValue("sendCode_" + phone);
        int cnt = 0;
        if(sendCount != null){
            cnt = Integer.parseInt(sendCount);
        }
        if(cnt >= 5){
            //短时间内发送短信验证码次数过多
            log.warn("用户2小时内发送短信验证码次数过多，账号已被锁定：" + phone);
            return "repeatWrong";
        }
        return "success";
    }

}