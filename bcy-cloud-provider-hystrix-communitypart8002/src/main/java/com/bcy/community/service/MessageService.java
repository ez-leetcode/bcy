package com.bcy.community.service;

import com.alibaba.fastjson.JSONObject;

public interface MessageService {

    JSONObject getAtMessageList(Long id,Long page,Long cnt);

    JSONObject getLikeMessageList(Long id,Long page,Long cnt);

    JSONObject getCommentMessageList(Long id,Long page,Long cnt);

    JSONObject getAllCounts(Long id);

    String allRead(Long id,Integer type);
}
