package com.bcy.quartz.job;

import com.bcy.quartz.mapper.UserMessageMapper;
import com.bcy.quartz.pojo.UserMessage;
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
public class UserNoReadCountsJob implements Job {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("正在更新用户未读计数数据");
        Map<Long,Integer> atMap = redisUtils.getAllRedisDataByKeys("atCounts");
        Map<Long,Integer> commentMap = redisUtils.getAllRedisDataByKeys("commentCounts");
        Map<Long,Integer> likeMap = redisUtils.getAllRedisDataByKeys("likeCounts");
        Map<Long,Integer> messageMap = redisUtils.getAllRedisDataByKeys("messageCounts");
        Long startTime = System.nanoTime();
        for(Long x:atMap.keySet()){
            //获取数据
            UserMessage userMessage = userMessageMapper.selectById(x);
            if(userMessage != null){
                userMessage.setAtCounts(atMap.get(x));
                userMessageMapper.updateById(userMessage);
                log.info("用户：" + userMessage.getId() + "的@未读计数已更新");
            }
            redisUtils.delete("atCounts_" + x);
        }
        for(Long x:commentMap.keySet()){
            //获取数据
            UserMessage userMessage = userMessageMapper.selectById(x);
            if(userMessage != null){
                userMessage.setCommentCounts(atMap.get(x));
                userMessageMapper.updateById(userMessage);
                log.info("用户：" + userMessage.getId() + "的评论未读计数已更新");
            }
            redisUtils.delete("commentCounts_" + x);
        }
        for(Long x:likeMap.keySet()){
            //获取数据
            UserMessage userMessage = userMessageMapper.selectById(x);
            if(userMessage != null){
                userMessage.setLikeCounts(likeMap.get(x));
                userMessageMapper.updateById(userMessage);
                log.info("用户：" + userMessage.getId() + "的点赞未读计数已更新");
            }
            redisUtils.delete("likeCounts_" + x);
        }
        for(Long x:messageMap.keySet()){
            //获取数据
            UserMessage userMessage = userMessageMapper.selectById(x);
            if(userMessage != null){
                userMessage.setMessageCounts(atMap.get(x));
                userMessageMapper.updateById(userMessage);
                log.info("用户：" + userMessage.getId() + "的消息未读计数已更新");
            }
            redisUtils.delete("messageCounts_" + x);
        }
        Long endTime = System.nanoTime();
        log.info("用户未读计数数据同步成功，总耗时:{}",(endTime - startTime) / 1000000 + "ms");
    }

}
