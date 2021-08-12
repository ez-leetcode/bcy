package com.bcy.community.service;

import com.alibaba.fastjson.JSONObject;

public interface BlackService {

    String addBlack(Long id,Long blackId);

    String deleteBlack(Long id,Long blackId);

    String addBlackCircle(Long id,String circleName);

    String deleteBlackCircle(Long id,String circleName);

    JSONObject getBlackList(Long id,Long page,Long cnt);

    JSONObject judgeBlack(Long id,Long toId);

    JSONObject judgeBlackCircle(Long id,String circleName);
}
