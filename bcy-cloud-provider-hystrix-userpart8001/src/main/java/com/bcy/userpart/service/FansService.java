package com.bcy.userpart.service;


import com.alibaba.fastjson.JSONObject;

public interface FansService {

    String addFollow(Long fromId,Long toId);

    String deleteFollow(Long fromId,Long toId);

    String judgeFollow(Long fromId,Long toId);

    JSONObject getFansList(Long id,String keyword,Long cnt,Long page);

    JSONObject getFollowList(Long id,String keyword,Long cnt,Long page);

}
