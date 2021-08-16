package com.bcy.elasticsearch.service;

import com.bcy.elasticsearch.utils.EsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CosServiceImpl implements CosService{

    @Autowired
    private EsUtils esUtils;

    @Override
    public String test(String indexName) {
        if(esUtils.isIndexExists(indexName)){
            return "success";
        }
        return "existWrong";
    }

}
