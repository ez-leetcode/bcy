package com.bcy.elasticsearch.utils;

import com.alibaba.fastjson.JSON;
import com.bcy.elasticsearch.dto.Ck;
import com.bcy.elasticsearch.dto.CosPlay;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@Component
public class EsUtils {

    @Resource
    private RestHighLevelClient restHighLevelClient;


    public boolean isIndexExists(String indexName){
        boolean exists = false;
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
            getIndexRequest.humanReadable(true);
            exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public void createIndex(String indexName)throws IOException{
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        CreateIndexResponse response = restHighLevelClient.indices().create(request,RequestOptions.DEFAULT);
        log.info(response.index());
        log.info(response.toString());
    }

    //es数据异步更新
    public void insertData(Object object,Integer type,Long number)throws IOException{
        IndexRequest request = new IndexRequest();
        request.type("_doc");
        request.id(number.toString());
        if(type == 1){
            request.index("cosplay");
        }else if(type == 2){
            request.index("qa");
        }
        String jsonString = JSON.toJSONString(object);
        log.info("待插入数据：" + jsonString);
        request.source(jsonString,XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(request,RequestOptions.DEFAULT);
        log.info(response.toString());
    }

    //数据删除
    public void deleteData(Integer type,Long number)throws IOException{
        DeleteRequest request = new DeleteRequest();
        request.id(number.toString());
        request.type("_doc");
        if(type == 1){
            request.index("cosplay");
        }else if(type == 2){
            request.index("qa");
        }
        log.info("待删除数据类型：" + type + " 编号：" + number);
        DeleteResponse response = restHighLevelClient.delete(request,RequestOptions.DEFAULT);
        log.info(response.toString());
    }

    //cos根据关键字搜索
    public void CosSearch(String keyword,Integer cnt,Integer page)throws IOException{
        SearchRequest request = new SearchRequest("cosplay");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //分页
        sourceBuilder.from(page);
        sourceBuilder.size(cnt);
        //搜索条件
        sourceBuilder.query(QueryBuilders.matchQuery("description",keyword));
        //sourceBuilder.sort("create_time", SortOrder.DESC);
        request.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
        log.info(response.status().toString());
        System.out.println(response.getHits().totalHits);
        if(response.getHits().totalHits > 0){
            SearchHits searchHits = response.getHits();
            for(SearchHit hit:searchHits){
                CosPlay cosPlay = JSON.parseObject(hit.getSourceAsString(),CosPlay.class);
                log.info(cosPlay.toString());
            }
        }
        log.info(response.toString());
    }

}