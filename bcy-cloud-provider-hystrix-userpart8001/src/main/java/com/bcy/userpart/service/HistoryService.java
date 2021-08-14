package com.bcy.userpart.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface HistoryService {

    JSONObject getHistory(Long id,Long cnt,Long page);

    JSONObject getQaHistory(Long id,Long cnt,Long page);

    String deleteHistory(Long id, List<Long> numbers);

    String deleteQaHistory(Long id,List<Long> numbers);

    String deleteAllHistory(Long id);

    String deleteQaAllHistory(Long id);
}
