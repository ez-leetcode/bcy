package com.bcy.elasticsearch.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.elasticsearch.dto.SearchHistory;
import com.bcy.elasticsearch.mapper.CircleFollowMapper;
import com.bcy.elasticsearch.mapper.SearchHistoryMapper;
import com.bcy.elasticsearch.utils.EsUtils;
import com.bcy.elasticsearch.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class QAServiceImpl implements QAService{

    @Autowired
    private EsUtils esUtils;

    @Autowired
    private SearchHistoryMapper searchHistoryMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CircleFollowMapper circleFollowMapper;

    @Override
    public JSONObject searchQa(Long id,String keyword, Integer cnt, Integer page) throws IOException {
        if(id != null && id != 0){
            //存入History
            QueryWrapper<SearchHistory> wrapper = new QueryWrapper<>();
            wrapper.eq("id",id)
                    .eq("keyword",keyword);
            SearchHistory searchHistory = searchHistoryMapper.selectOne(wrapper);
            if(searchHistory != null){
                searchHistory.setDeleted(0);
                searchHistory.setReClick(searchHistory.getReClick() + 1);
                searchHistoryMapper.updateById(searchHistory);
            }else{
                //插入历史
                searchHistoryMapper.insert(new SearchHistory(null,id,keyword,0,0,null));
            }
            log.info("正在添加问答历史搜索");
            //维护redis
            Page<String> page1 = new Page<>(1,20);
            List<String> historyList = searchHistoryMapper.getHistoryKeywordList(id,page1);
            redisUtils.saveByHoursTime("keyword_" + id,historyList.toString(),48);
        }
        return esUtils.QaSearch(keyword, cnt, page);
    }


    @Override
    public JSONObject getRecommendQa(Long id, Integer cnt) throws IOException {
        //先获取关注圈子
        List<String> followCircleList = circleFollowMapper.getCircleFollowList(id);
        followCircleList.add("是 否 吗 么 能 不 还 如何 怎么 可以 测试 test 1 2 3 4 5 6 7 8 9 0");
        String keyword = followCircleList.toString();
        log.info(keyword);
        JSONObject jsonObject = esUtils.recommendQa(keyword,cnt);
        log.info("获取推荐问答成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }
}
