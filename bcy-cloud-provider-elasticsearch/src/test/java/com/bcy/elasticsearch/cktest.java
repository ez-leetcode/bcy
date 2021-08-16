package com.bcy.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.bcy.elasticsearch.ElasticsearchMain;
import com.bcy.elasticsearch.utils.EsUtils;
import com.bcy.vo.UserInfo;
import com.bcy.vo.UserInfoForSearchList;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest
public class cktest{

    @Autowired
    private EsUtils esUtils;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void fun() throws IOException {
        System.out.println(
                esUtils.isIndexExists("aaaa"));
        System.out.println(esUtils.isIndexExists("test"));
    }


}
