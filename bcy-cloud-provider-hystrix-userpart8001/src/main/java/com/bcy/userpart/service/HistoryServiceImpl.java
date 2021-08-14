package com.bcy.userpart.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.userpart.mapper.*;
import com.bcy.userpart.pojo.*;
import com.bcy.utils.PhotoUtils;
import com.bcy.vo.CosHistoryForList;
import com.bcy.vo.QaHistoryForList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@Slf4j
public class HistoryServiceImpl implements HistoryService{

    @Autowired
    private HistoryMapper historyMapper;

    @Autowired
    private QaHistoryMapper qaHistoryMapper;

    @Autowired
    private QaMapper qaMapper;

    @Autowired
    private QaAnswerMapper qaAnswerMapper;

    @Autowired
    private CosMapper cosMapper;

    @Autowired
    private CosCommentMapper cosCommentMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getHistory(Long id, Long cnt, Long page) {
        JSONObject jsonObject = new JSONObject();
        Page<CosHistoryForList> page1 = new Page<>(page,cnt);
        List<CosHistoryForList> cosHistoryForList = cosMapper.getCosHistoryForList(id,page1);
        for(CosHistoryForList x:cosHistoryForList){
            User user = userMapper.selectById(x.getId());
            if(user != null){
                x.setUsername(user.getUsername());
                x.setPhoto(user.getPhoto());
            }
            Cos cos =cosMapper.selectById(x.getId());
            if(cos != null){
                x.setCosPhoto(PhotoUtils.photoStringToList(cos.getPhoto()));
            }
        }
        jsonObject.put("historyCosList",cosHistoryForList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取cos历史记录成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getQaHistory(Long id, Long cnt, Long page) {
        JSONObject jsonObject = new JSONObject();
        Page<QaHistoryForList> page1 = new Page<>(page,cnt);
        List<QaHistoryForList> qaHistoryForListList = qaHistoryMapper.getQaHistory(id,page1);
        for(QaHistoryForList x:qaHistoryForListList){
            Qa qa = qaMapper.selectById(x.getId());
            if(qa != null){
                x.setQaPhoto(PhotoUtils.photoStringToList(qa.getPhoto()));
            }
            User user = userMapper.selectById(x.getId());
            if(user != null){
                x.setUsername(user.getUsername());
                x.setPhoto(user.getPhoto());
            }
        }
        jsonObject.put("historyQaList",qaHistoryForListList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取问答历史记录成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public String deleteQaHistory(Long id, List<Long> numbers) {
        int result = qaHistoryMapper.deleteBatchIds(numbers);
        if(result == 0){
            log.warn("清除历史浏览异常，问答历史记录可能已被清空");
            return "existWrong";
        }
        log.info("清除问答历史记录成功，清空了：" + result + "条");
        return "success";
    }

    @Override
    public String deleteQaAllHistory(Long id) {
        QueryWrapper<QaHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        int result = qaHistoryMapper.delete(wrapper);
        log.info("清空问答历史浏览成功，共清空了：" + result + "条数据");
        return "success";
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