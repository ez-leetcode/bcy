package com.bcy.quartz.job;

import com.bcy.quartz.mapper.CosCountsMapper;
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
public class CosCountsJob implements Job {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CosCountsMapper cosCountsMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("正在更新cos的计数数据");
        Map<Long,Integer> favorMap = redisUtils.getAllRedisDataByKeys("cosFavorCounts");
        Map<Long,Integer> likeMap = redisUtils.getAllRedisDataByKeys("cosLikeCounts");
        Map<Long,Integer> commentMap = redisUtils.getAllRedisDataByKeys("cosCommentCounts");
        Map<Long,Integer> shareMap = redisUtils.getAllRedisDataByKeys("cosShareCounts");
        //8.5
    }

}
