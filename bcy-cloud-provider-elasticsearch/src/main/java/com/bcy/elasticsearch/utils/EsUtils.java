package com.bcy.elasticsearch.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bcy.elasticsearch.dto.*;
import com.bcy.elasticsearch.mapper.UserMapper;
import com.bcy.vo.CosForSearchList;
import com.bcy.vo.QaForSearchList;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Slf4j
@Component
public class EsUtils {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private UserMapper userMapper;


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

    //es??????????????????
    public void insertData(Object object,Integer type,Long number)throws IOException{
        IndexRequest request = new IndexRequest();
        request.type("_doc");
        request.id(number.toString());
        if(type == 1){
            request.index("cosplay");
        }else if(type == 2){
            request.index("qa");
        }else if(type == 3){
            request.index("circle");
        }else{
            request.index("user");
        }
        String jsonString = JSON.toJSONString(object);
        log.info("??????????????????" + jsonString);
        request.source(jsonString,XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(request,RequestOptions.DEFAULT);
        log.info(response.toString());
    }

    //????????????
    public void deleteData(Integer type,Long number)throws IOException{
        DeleteRequest request = new DeleteRequest();
        request.id(number.toString());
        request.type("_doc");
        if(type == 1){
            request.index("cosplay");
        }else if(type == 2){
            request.index("qa");
        }else if(type == 3){
            request.index("circle");
        }else{
            request.index("user");
        }
        log.info("????????????????????????" + type + " ?????????" + number);
        DeleteResponse response = restHighLevelClient.delete(request,RequestOptions.DEFAULT);
        log.info(response.toString());
    }

    //??????????????????
    public JSONObject QaSearch(String keyword,Integer cnt,Integer page)throws IOException{
        JSONObject jsonObject = new JSONObject();
        SearchRequest request = new SearchRequest("qa");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(page - 1);
        sourceBuilder.size(cnt);
        //???????????? ?????? ?????? ??????
        sourceBuilder.query(QueryBuilders.multiMatchQuery(keyword,"title","description","label"));
        request.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
        log.info(response.toString());
        log.info("???????????????" + response.status().toString() + " ??????" + response.getHits().totalHits + "?????????");
        List<QaForEs> qaForEsList = new LinkedList<>();
        if(response.getHits().totalHits > 0){
            SearchHits searchHits = response.getHits();
            for(SearchHit hit:searchHits){
                QaForEs qaForEs = JSON.parseObject(hit.getSourceAsString(),QaForEs.class);
                qaForEsList.add(qaForEs);
                log.info(qaForEs.toString());
            }
        }
        List<QaForSearchList> qaForSearchListList = new LinkedList<>();
        for(QaForEs x:qaForEsList){
            QaForSearchList qaForSearchList = new QaForSearchList(x.getNumber(),x.getId(),null,null,x.getDescription(),x.getTitle(),x.getLabel(),x.getPhoto(),x.getCreateTime());
            User user = userMapper.selectById(x.getId());
            if(user != null){
                qaForSearchList.setUsername(user.getUsername());
                qaForSearchList.setPhoto(user.getPhoto());
            }
            qaForSearchListList.add(qaForSearchList);
        }
        jsonObject.put("qaList",qaForSearchListList);
        if(response.getHits().totalHits == 0){
            jsonObject.put("pages",0);
        }else{
            long ck1 = response.getHits().totalHits % cnt;
            long ck = response.getHits().totalHits / cnt;
            if(ck1 != 0){
                ck ++;
            }
            jsonObject.put("pages",ck);
        }
        jsonObject.put("counts",response.getHits().totalHits);
        log.info("??????????????????");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    //circle??????????????? ?????? ????????????
    public JSONObject RecommendCircle(String searchWord,Integer cnt,Integer page)throws IOException{
        JSONObject jsonObject = new JSONObject();
        SearchRequest request = new SearchRequest("circle");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //??????
        sourceBuilder.from(page - 1);
        sourceBuilder.size(cnt);
        //????????????
        sourceBuilder.query(QueryBuilders.multiMatchQuery(searchWord,"circleName","nickName","description"));
        request.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
        log.info(response.toString());
        log.info("???????????????" + response.status().toString() + " ??????" + response.getHits().totalHits + "?????????");
        List<CircleForEs> circleForEsList = new LinkedList<>();
        if(response.getHits().totalHits > 0){
            SearchHits searchHits = response.getHits();
            for(SearchHit hit:searchHits){
                CircleForEs circleForEs = JSON.parseObject(hit.getSourceAsString(),CircleForEs.class);
                circleForEsList.add(circleForEs);
                log.info(circleForEs.toString());
            }
        }
        long ck = response.getHits().totalHits / cnt;
        long ck1 = response.getHits().totalHits % cnt;
        if(ck1 != 0){
            ck ++;
        }
        jsonObject.put("recommendCircleList",circleForEsList);
        jsonObject.put("pages",ck);
        jsonObject.put("counts",response.getHits().totalHits);
        log.info("????????????????????????");
        log.info(jsonObject.toString());
        return jsonObject;
    }


    public JSONObject recommendQa(String keyword,Integer cnt)throws IOException{
        JSONObject jsonObject = new JSONObject();
        SearchRequest request = new SearchRequest("qa");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.multiMatchQuery(keyword,"title","description","label"));
        sourceBuilder.size(100);
        request.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
        log.info(response.toString());
        log.info("???????????????" + response.status().toString() + " ??????" + response.getHits().totalHits + "?????????" + " ?????????????????????" + cnt + "???");
        List<QaForEs> qaForEsList = new LinkedList<>();
        if(response.getHits().totalHits > 0){
            SearchHits searchHits = response.getHits();
            Random random = new Random();
            while(cnt > 0){
                cnt --;
                int pos = random.nextInt((int)response.getHits().totalHits);
                SearchHit searchHit = searchHits.getAt(pos);
                QaForEs qaForEs = JSON.parseObject(searchHit.getSourceAsString(),QaForEs.class);
                qaForEsList.add(qaForEs);
                log.info(qaForEs.toString());
            }
        }
        List<QaForSearchList> qaForSearchListList = new LinkedList<>();
        for(QaForEs x:qaForEsList){
            QaForSearchList qaForSearchList = new QaForSearchList(x.getNumber(),x.getId(),null,null,x.getDescription(),
                    x.getTitle(),x.getLabel(),x.getPhoto(),x.getCreateTime());
            User user = userMapper.selectById(x.getId());
            if(user != null){
                qaForSearchList.setUsername(user.getUsername());
                qaForSearchList.setPhoto(user.getPhoto());
                log.info(qaForSearchList.toString());
            }
            qaForSearchListList.add(qaForSearchList);
        }
        jsonObject.put("qaList",qaForSearchListList);
        return jsonObject;
    }

    public JSONObject recommendCos(String keyword,Integer cnt)throws IOException{
        JSONObject jsonObject = new JSONObject();
        SearchRequest request = new SearchRequest("cosplay");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.multiMatchQuery(keyword,"description","label"));
        sourceBuilder.size(200);
        request.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
        log.info(response.toString());
        log.info("???????????????" + response.status().toString() + " ??????" + response.getHits().totalHits + "?????????" + " ?????????????????????" + cnt + "???");
        List<CosPlayForEs> cosPlayForEsList = new LinkedList<>();
        if(response.getHits().totalHits > 0){
            SearchHits searchHits = response.getHits();
            Random random = new Random();
            while(cnt > 0){
                cnt --;
                int pos = random.nextInt((int)response.getHits().totalHits);
                SearchHit searchHit = searchHits.getAt(pos);
                CosPlayForEs cosPlayForEs = JSON.parseObject(searchHit.getSourceAsString(),CosPlayForEs.class);
                cosPlayForEsList.add(cosPlayForEs);
                log.info(cosPlayForEs.toString());
            }
        }
        List<CosForSearchList> cosForSearchListList = new LinkedList<>();
        log.info(cosPlayForEsList.toString());
        for(CosPlayForEs x:cosPlayForEsList){
            User user = userMapper.selectById(x.getId());
            CosForSearchList cosForSearchList = new CosForSearchList(x.getNumber(),x.getId(),null,null,x.getDescription(),x.getLabel(),
                    x.getPhoto(), x.getCreateTime());
            if(user != null){
                cosForSearchList.setUsername(user.getUsername());
                cosForSearchList.setPhoto(user.getPhoto());
            }
            log.info(cosForSearchList.toString());
            cosForSearchListList.add(cosForSearchList);
        }
        jsonObject.put("cosList",cosForSearchListList);
        return jsonObject;
    }

    //cos???????????????????????????
    public JSONObject CosSearch(String keyword, Integer cnt, Integer page)throws IOException{
        JSONObject jsonObject = new JSONObject();
        SearchRequest request = new SearchRequest("cosplay");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //??????
        //????????????0????????????
        sourceBuilder.from(page - 1);
        sourceBuilder.size(cnt);
        //????????????,??????
        sourceBuilder.query(QueryBuilders.multiMatchQuery(keyword,"description","label"));
        //sourceBuilder.sort("create_time", SortOrder.DESC);
        request.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
        log.info(response.toString());
        log.info("???????????????" + response.status().toString() + " ??????" + response.getHits().totalHits + "?????????");
        List<CosPlayForEs> cosPlayForEsList = new LinkedList<>();
        if(response.getHits().totalHits > 0){
            SearchHits searchHits = response.getHits();
            for(SearchHit hit:searchHits){
                CosPlayForEs cosPlayForEs = JSON.parseObject(hit.getSourceAsString(),CosPlayForEs.class);
                cosPlayForEsList.add(cosPlayForEs);
                log.info(cosPlayForEs.toString());
            }
        }
        List<CosForSearchList> cosForSearchListList = new LinkedList<>();
        for(CosPlayForEs x:cosPlayForEsList){
            CosForSearchList cosForSearchList = new CosForSearchList(x.getNumber(),x.getId(),null,null,x.getDescription(),x.getLabel(),
                    x.getPhoto(), x.getCreateTime());
            User user = userMapper.selectById(x.getId());
            if(user != null){
                cosForSearchList.setUsername(user.getUsername());
                cosForSearchList.setPhoto(user.getPhoto());
            }
            cosForSearchListList.add(cosForSearchList);
        }
        jsonObject.put("cosList",cosForSearchListList);
        jsonObject.put("counts",response.getHits().totalHits);
        if(response.getHits().totalHits == 0){
            jsonObject.put("pages",0);
        }else{
            long ck = response.getHits().totalHits / cnt;
            long ck1 = response.getHits().totalHits % cnt;
            if(ck1 != 0){
                ck ++;
            }
            jsonObject.put("pages",ck);
        }
        log.info(jsonObject.toString());
        return jsonObject;
    }

}