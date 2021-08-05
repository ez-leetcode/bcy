package com.bcy.quartz.job;

import com.bcy.quartz.mapper.CosCountsMapper;
import com.bcy.quartz.pojo.CosCounts;
import com.bcy.quartz.pojo.Qa;
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
        Long startTime = System.nanoTime();
        for(Long x:favorMap.keySet()){
            //获取数据
            CosCounts cosCounts = cosCountsMapper.selectById(x);
            if(cosCounts != null){
                cosCounts.setFavorCounts(favorMap.get(x));
                cosCountsMapper.updateById(cosCounts);
                log.info("cos编号：" + cosCounts.getNumber() + "的cos收藏数已添加");
            }
            redisUtils.delete("cosFavorCounts_" + x);
        }
        for(Long x:likeMap.keySet()){
            //获取数据
            CosCounts cosCounts = cosCountsMapper.selectById(x);
            if(cosCounts != null){
                cosCounts.setLikeCounts(likeMap.get(x));;
                cosCountsMapper.updateById(cosCounts);
                log.info("cos编号：" + cosCounts.getNumber() + "的cos点赞次数已添加");
            }
            redisUtils.delete("cosLikeCounts_" + x);
        }
        for(Long x:commentMap.keySet()){
            //获取数据
            CosCounts cosCounts = cosCountsMapper.selectById(x);
            if(cosCounts != null){
                cosCounts.setCommentCounts(commentMap.get(x));
                cosCountsMapper.updateById(cosCounts);
                log.info("cos编号：" + cosCounts.getNumber() + "的cos评论次数已添加");
            }
            redisUtils.delete("cosCommentCounts_" + x);
        }
        for(Long x:shareMap.keySet()){
            //获取数据
            CosCounts cosCounts = cosCountsMapper.selectById(x);
            if(cosCounts != null){
                cosCounts.setShareCounts(shareMap.get(x));
                cosCountsMapper.updateById(cosCounts);
                log.info("cos编号：" + cosCounts.getNumber() + "的cos分享次数已添加");
            }
            redisUtils.delete("cosShareCounts_" + x);
        }
        Long endTime = System.nanoTime();
        log.info("cos数据同步成功，总耗时:{}",(endTime - startTime) / 1000000 + "ms");
    }

}
