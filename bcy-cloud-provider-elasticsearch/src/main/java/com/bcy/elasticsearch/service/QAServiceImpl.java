package com.bcy.elasticsearch.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.elasticsearch.utils.EsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class QAServiceImpl implements QAService{

    @Autowired
    private EsUtils esUtils;

    @Override
    public JSONObject searchQa(String keyword, Integer cnt, Integer page) throws IOException {
        return esUtils.QaSearch(keyword, cnt, page);
    }

}
