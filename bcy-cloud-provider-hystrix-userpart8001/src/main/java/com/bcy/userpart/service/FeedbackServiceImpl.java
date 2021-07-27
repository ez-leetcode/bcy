package com.bcy.userpart.service;

import com.bcy.userpart.mapper.FeedbackMapper;
import com.bcy.userpart.pojo.Feedback;
import com.bcy.userpart.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FeedbackServiceImpl implements FeedbackService{

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public String addFeedback(Long id, String description) {
        //先看看有没有超过5次
        String cnt = redisUtils.getValue("feedback_" + id.toString());
        int i = 0;
        if(cnt != null) {
            i = Integer.getInteger(cnt);
            if (i >= 5) {
                log.info("添加用户反馈失败，反馈次数过多，已反馈次数：" + i);
                return "repeatWrong";
            }
        }
        //没超过，添加反馈
        feedbackMapper.insert(new Feedback(null,id,description,0,null));
        //增加对应次数
        redisUtils.addKeyByTime("feedback_" + id.toString(),i + 1);
        log.info("添加用户反馈成功");
        return "success";
    }

}