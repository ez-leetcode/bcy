package com.bcy.quartz.job;

import com.bcy.quartz.mapper.CosCommentMapper;
import com.bcy.quartz.pojo.CosComment;
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
public class CosCommentsCountsJob implements Job {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CosCommentMapper cosCommentMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<Long,Integer> likeCountsMap = redisUtils.getAllRedisDataByKeys("cosCommentLikeCounts");
        Map<Long,Integer> commentCountsMap = redisUtils.getAllRedisDataByKeys("cosCommentCommentCounts");
        Long startTime = System.nanoTime();
        for(Long x:likeCountsMap.keySet()){
            //获取数据
            CosComment cosComment = cosCommentMapper.selectById(x);
            if(cosComment != null){
                cosComment.setLikeCounts(likeCountsMap.get(x));
                cosCommentMapper.updateById(cosComment);
                log.info("cos评论编号：" + cosComment.getNumber() + "的cos评论的点赞次数已添加");
            }
            redisUtils.delete("cosCommentLikeCounts_" + x);
        }
        for(Long x:commentCountsMap.keySet()){
            //获取数据
            CosComment cosComment = cosCommentMapper.selectById(x);
            if(cosComment != null){
                cosComment.setCommentCounts(commentCountsMap.get(x));
                cosCommentMapper.updateById(cosComment);
                log.info("cos评论编号：" + cosComment.getNumber() + "的cos评论的评论次数已添加");
            }
            redisUtils.delete("cosCommentCommentCounts_" + x);
        }
        Long endTime = System.nanoTime();
        log.info("cos评论数据同步成功，总耗时:{}",(endTime - startTime) / 1000000 + "ms");
    }
}
