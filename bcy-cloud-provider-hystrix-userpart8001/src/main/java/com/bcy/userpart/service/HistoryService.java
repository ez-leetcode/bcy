package com.bcy.userpart.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface HistoryService {

    JSONObject getHistory(Long id,String keyword,Long cnt,Long page);

    String deleteHistory(Long id, List<Long> numbers);

    String deleteAllHistory(Long id);

}
