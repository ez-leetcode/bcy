package com.bcy.userpart.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcy.userpart.mapper.HistoryMapper;
import com.bcy.userpart.pojo.History;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@Slf4j
public class HistoryServiceImpl implements HistoryService{

    @Autowired
    private HistoryMapper historyMapper;

    @Override
    public JSONObject getHistory(Long id, String keyword, Long cnt, Long page) {
        return null;
    }

    @Override
    public String deleteHistory(Long id, List<Long> numbers) {
        int result = historyMapper.deleteBatchIds(numbers);
        if(result == 0){
            log.warn("清除历史浏览异常，历史记录可能已被清空");
            return "existWrong";
        }
        log.info("清除历史记录成功，清空了：" + result + "条");
        return "success";
    }

    @Override
    public String deleteAllHistory(Long id) {
        QueryWrapper<History> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        int result = historyMapper.delete(wrapper);
        log.info("清空历史浏览成功，共清空了：" + result + "条数据");
        return "success";
    }

}
