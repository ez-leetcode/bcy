package com.bcy.elasticsearch.service;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public interface CosService {

    String test(String indexName) throws IOException;

    JSONObject searchCos(Long id, String keyword, Integer cnt, Integer page)throws IOException;

    JSONObject getSearchKeywordList(Long id);

    JSONObject getLabelCos(Long id,String keyword,Integer cnt,Integer page) throws IOException;

    JSONObject getRecommendCos(Long id,Integer cnt) throws IOException;

    String deleteHistory(Long id,String keyword);

    String deleteAllHistory(Long id);
}
