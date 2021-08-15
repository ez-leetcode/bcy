package com.bcy.community.service;

import com.alibaba.fastjson.JSONObject;

public interface HomePageService {

    JSONObject getOthersInfo(Long id);

    JSONObject searchUser(Long id,Long page,Long cnt,String keyword);
}
