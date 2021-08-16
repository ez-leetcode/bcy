package com.bcy.quartz.job;

import com.bcy.quartz.mapper.QaAnswerMapper;
import com.bcy.quartz.mapper.QaCommentMapper;
import com.bcy.quartz.pojo.QaAnswer;
import com.bcy.quartz.pojo.QaComment;
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
public class QAAnswerCommentCountsJob implements Job {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private QaCommentMapper qaCommentMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("正在更新回答评论计数数据");
        Map<Long,Integer> likeMap = redisUtils.getAllRedisDataByKeys("likeQaComment");
        Map<Long,Integer> commentMap = redisUtils.getAllRedisDataByKeys("qaAnswerCommentCommentCounts");
        Long startTime = System.nanoTime();
        for(Long x:likeMap.keySet()){
            //获取数据
            QaComment qaComment = qaCommentMapper.selectById(x);
            if(qaComment != null){
                qaComment.setLikeCounts(likeMap.get(x));
                qaCommentMapper.updateById(qaComment);
                log.info("问答回复评论编号：" + qaComment.getNumber() + "的问答回复评论点赞次数已添加");
            }
            redisUtils.delete("likeQaComment_" + x);
        }
        for(Long x:commentMap.keySet()){
            //获取数据
            QaComment qaComment = qaCommentMapper.selectById(x);
            if(qaComment != null){
                qaComment.setCommentCounts(commentMap.get(x));
                qaCommentMapper.updateById(qaComment);
                log.info("问答回复评论编号：" + qaComment.getNumber() + "的问答回复的评论次数已添加");
            }
            redisUtils.delete("qaAnswerCommentCommentCounts_" + x);
        }
        Long endTime = System.nanoTime();
        log.info("问答的回答的评论数据同步成功，总耗时:{}",(endTime - startTime) / 1000000 + "ms");
    }
}
