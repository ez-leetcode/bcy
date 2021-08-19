package com.bcy.elasticsearch.service;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public interface QAService {

    JSONObject searchQa(String keyword,Integer cnt,Integer page)throws IOException;

}
