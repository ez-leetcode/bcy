package com.bcy.elasticsearch.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.elasticsearch.dto.SearchHistory;
import com.bcy.elasticsearch.mapper.SearchHistoryMapper;
import com.bcy.elasticsearch.utils.EsUtils;
import com.bcy.elasticsearch.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class CosServiceImpl implements CosService{

    @Autowired
    private EsUtils esUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SearchHistoryMapper searchHistoryMapper;

    @Override
    public String test(String indexName) throws IOException {
        return "success";
    }

    @Override
    public JSONObject searchCos(Long id, String keyword, Integer cnt, Integer page) throws IOException {
        JSONObject jsonObject = esUtils.CosSearch(keyword, cnt, page);
        //id待加入
        if(id != null && id != 0){
            //存入History
            QueryWrapper<SearchHistory> wrapper = new QueryWrapper<>();
            wrapper.eq("id",id)
                    .eq("keyword",keyword);
            SearchHistory searchHistory = searchHistoryMapper.selectOne(wrapper);
            log.info("正在添加cos历史搜索");
            if(searchHistory != null){
                searchHistory.setReClick(searchHistory.getReClick() + 1);
                searchHistoryMapper.updateById(searchHistory);
            }else{
                //插入历史
                searchHistoryMapper.insert(new SearchHistory(null,id,keyword,0,null));
            }
            //维护redis
            Page<String> page1 = new Page<>(1,20);
            List<String> historyList = searchHistoryMapper.getHistoryKeywordList(id,page1);
            redisUtils.saveByHoursTime("keyword_" + id,historyList.toString(),48);
        }
        return jsonObject;
    }

    @Override
    public JSONObject getSearchKeywordList(Long id) {
        JSONObject jsonObject = new JSONObject();
        //先从redis里找，有直接返回
        String keyword = redisUtils.getValue("keyword_" + id);
        if(keyword != null){
            jsonObject.put("keywordList",keyword);
        }else{
            //从history里找
            Page<String> page = new Page<>(1,20);
            List<String> historyList = searchHistoryMapper.getHistoryKeywordList(id,page);
            //存入redis
            redisUtils.saveByHoursTime("keyword_" + id,historyList.toString(),48);
            jsonObject.put("keywordList",historyList.toString());
        }
        log.info("获取搜索关键词成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }


    @Override
    public String deleteHistory(Long id, String keyword) {
        //删除mysql
        QueryWrapper<SearchHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id)
                .eq("keyword",keyword);
        searchHistoryMapper.delete(wrapper);
        //维护redis
        Page<String> page1 = new Page<>(1,20);
        List<String> historyList = searchHistoryMapper.getHistoryKeywordList(id,page1);
        redisUtils.saveByHoursTime("keyword_" + id,historyList.toString(),48);
        log.info("移除历史记录成功");
        return "success";
    }

    @Override
    public String deleteAllHistory(Long id) {
        //删mysql
        QueryWrapper<SearchHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        searchHistoryMapper.delete(wrapper);
        //清除redis缓存
        redisUtils.delete("keyword_" + id);
        log.info("移除所有历史记录成功");
        return "success";
    }

}