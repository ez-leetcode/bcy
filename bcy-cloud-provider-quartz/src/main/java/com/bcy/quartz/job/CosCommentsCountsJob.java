package com.bcy.quartz.job;

import com.bcy.quartz.mapper.CosCommentMapper;
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
    }

}
