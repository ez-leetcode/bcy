package com.bcy.userpart.service;

public interface SmsService {

    boolean sendSms(String phone,String code,int templateCode);

    String judgeCode(String phone,Integer type);

    String changePhone(Long id,String phone,String code);
}
