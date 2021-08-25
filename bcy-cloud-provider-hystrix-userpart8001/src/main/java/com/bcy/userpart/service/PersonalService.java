package com.bcy.userpart.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PersonalService {

    String userPhotoUpload(MultipartFile file,Long id);

    String changeInfo(Long id,String username,String sex,String description,String province,String city,String birthday);

    String changeSetting(Long id,Integer pushComment,Integer pushLike,Integer pushFans,Integer pushSystem,Integer pushInfo);

    JSONObject getPersonalSetting(Long id);

    JSONObject getPersonalInfo(Long id,String phone);

    JSONObject getUserCounts(List<Long> userId);

    JSONObject judgeNew(Long id);

    JSONObject getCommunityInfo(Long id);

    String setPassword(Long id,String password);
}