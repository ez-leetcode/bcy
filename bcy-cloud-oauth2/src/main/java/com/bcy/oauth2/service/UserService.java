package com.bcy.oauth2.service;

public interface UserService {

    String changePassword(String phone,String newPassword,String code);

    String loginByCode(String phone,String code,Integer type);

    String logout(Long id,Integer type);
}
