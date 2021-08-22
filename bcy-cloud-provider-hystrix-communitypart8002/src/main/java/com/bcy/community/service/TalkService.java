package com.bcy.community.service;


import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.util.List;

public interface TalkService {

    JSONObject getJudgeOnline(List<Long> userId);

    String deleteTalkMessage(Long id,List<Long> number);

    String deleteTalk(Long id,Long toId);

    JSONObject getTalkCounts(Long id,List<Long> toId) throws ParseException;

    JSONObject getTalkList(Long id,Long page,Long cnt);

    JSONObject getP2PTalkList(Long id,Long toId,Long cnt,Long page);

    String allRead(Long id,Long toId);
}
