package com.bcy.community.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.community.mapper.AskMapper;
import com.bcy.community.mapper.UserMapper;
import com.bcy.community.mapper.UserSettingMapper;
import com.bcy.community.pojo.Ask;
import com.bcy.community.pojo.User;
import com.bcy.community.pojo.UserSetting;
import com.bcy.mq.AskMsg;
import com.bcy.vo.AskForAnswer;
import com.bcy.vo.AskForList;
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

    @Autowired
    private UserSettingMapper userSettingMapper;

    @Autowired
    private RabbitmqProducerService rabbitmqProducerService;

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
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        QueryWrapper<Ask> wrapper = new QueryWrapper<>();
        wrapper.eq("question",question)
                .eq("from_id",fromId)
                .eq("to_id",toId)
                .orderByDesc("create_time");
        List<Ask> asksList = askMapper.selectList(wrapper);
        //现在还能提问说明没被封号
        User user = userMapper.selectById(fromId);
        UserSetting userSetting = userSettingMapper.selectById(fromId);
        //推送提问
        if(userSetting.getPushInfo() == 1){
            rabbitmqProducerService.sendAskMessage(new AskMsg(asksList.get(0).getNumber(),toId,user.getUsername(),question));
        }
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
        askMapper.updateById(ask);
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

    @Override
    public JSONObject getAskList(Long id, Long page, Long cnt) {
        JSONObject jsonObject = new JSONObject();
        Page<AskForList> page1 = new Page<>(page,cnt);
        List<AskForList> askForList = askMapper.getAskList(id,page1);
        jsonObject.put("askList",askForList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取问题列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }
}