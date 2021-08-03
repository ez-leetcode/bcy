package com.bcy.community.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.community.mapper.AskMapper;
import com.bcy.community.mapper.UserMapper;
import com.bcy.community.pojo.Ask;
import com.bcy.vo.AskForAnswer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class AskServiceImpl implements AskService{

    @Autowired
    private AskMapper askMapper;

    @Autowired
    private UserMapper userMapper;

    //推送待完成

    @Override
    public String deleteAsk(Long id, Long number) {
        Ask ask = askMapper.selectById(number);
        if(ask == null || !ask.getToId().equals(id)){
            log.error("删除提问失败，提问不存在或已被删除或用户不匹配");
            return "existWrong";
        }
        askMapper.deleteById(number);
        log.info("删除提问成功");
        return "success";
    }

    //黑名单待完成
    @Override
    public String addAsk(Long fromId, Long toId, String question) {
        askMapper.insert(new Ask(null,question,null,fromId,toId,0,null));
        log.info("向用户提问成功");
        return "success";
    }

    //推送待完成
    @Override
    public String addAnswer(Long id, Long number, String answer) {
        Ask ask = askMapper.selectById(number);
        if(ask == null || ask.getAnswer() != null || !ask.getToId().equals(id)){
            log.error("回答问题失败，提问不存在或已被回答或不是本人");
            return "existWrong";
        }
        ask.setAnswer(answer);
        log.info("回答问题成功");
        return "success";
    }


    @Override
    public JSONObject getWaitingAsk(Long id, Long page, Long cnt) {
        JSONObject jsonObject = new JSONObject();
        Page<AskForAnswer> page1 = new Page<>(page,cnt);
        List<AskForAnswer> askForAnswerList = askMapper.getWaitingAskList(id,page1);
        jsonObject.put("waitingAskList",askForAnswerList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取待回应提问成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

}