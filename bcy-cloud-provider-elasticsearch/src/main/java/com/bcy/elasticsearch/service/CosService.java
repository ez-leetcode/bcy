package com.bcy.elasticsearch.service;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public interface CosService {

    String test(String indexName) throws IOException;

    JSONObject searchCos(String keyword, Integer cnt, Integer page)throws IOException;

}
