package com.bcy.community.service;

import com.alibaba.fastjson.JSONObject;

public interface AskService {

    String deleteAsk(Long id,Long number);

    String addAsk(Long fromId,Long toId,String question);

    String addAnswer(Long id,Long number,String answer);

    JSONObject getWaitingAsk(Long id,Long page,Long cnt);

    JSONObject getAskList(Long id,Long page,Long cnt);
}
