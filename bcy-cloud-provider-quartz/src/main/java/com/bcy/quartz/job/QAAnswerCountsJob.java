package com.bcy.quartz.job;

import com.bcy.quartz.mapper.QaAnswerMapper;
import com.bcy.quartz.pojo.Qa;
import com.bcy.quartz.pojo.QaAnswer;
import com.bcy.quartz.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class QAAnswerCountsJob implements Job {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private QaAnswerMapper qaAnswerMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("正在更新问答回答计数数据");
        Map<Long,Integer> likeMap = redisUtils.getAllRedisDataByKeys("likeQaAnswer");
        Map<Long,Integer> commentMap = redisUtils.getAllRedisDataByKeys("qaAnswerCommentCounts");
        Long startTime = System.nanoTime();
        for(Long x:likeMap.keySet()){
            //获取数据
            QaAnswer qaAnswer = qaAnswerMapper.selectById(x);
            if(qaAnswer != null){
                qaAnswer.setLikeCounts(likeMap.get(x));
                qaAnswerMapper.updateById(qaAnswer);
                log.info("问答回复编号：" + qaAnswer.getNumber() + "的问答回复点赞次数已添加");
            }
            redisUtils.delete("likeQaAnswer_" + x);
        }
        for(Long x:commentMap.keySet()){
            //获取数据
            QaAnswer qaAnswer = qaAnswerMapper.selectById(x);
            if(qaAnswer != null){
                qaAnswer.setCommentCounts(commentMap.get(x));
                qaAnswerMapper.updateById(qaAnswer);
                log.info("问答回复编号：" + qaAnswer.getNumber() + "的问答回复的评论次数已添加");
            }
            redisUtils.delete("qaAnswerCommentCounts_" + x);
        }
        Long endTime = System.nanoTime();
        log.info("问答的回答数据同步成功，总耗时:{}",(endTime - startTime) / 1000000 + "ms");
    }

}
