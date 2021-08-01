package com.bcy.oauth2.service;

public interface SmsService {

    boolean sendSms(String phone,String code,int templateCode);

    String judgeCode(String phone,Integer type);

}
