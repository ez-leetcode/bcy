package com.bcy.quartz.job;

import com.bcy.quartz.mapper.CircleMapper;
import com.bcy.quartz.pojo.Circle;
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
public class CircleCountsJob implements Job {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CircleMapper circleMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("正在更新圈子计数数据");
        Map<String,Integer> postCountsMap = redisUtils.getAllStringRedisDataByKeys("circlePostCounts");
        Long startTime = System.nanoTime();
        for(String x:postCountsMap.keySet()){
            //获取数据
            String real = x.substring(17);
            Circle circle = circleMapper.selectById(real);
            if(circle != null){
                circle.setPostCounts(postCountsMap.get(x));
                circleMapper.updateById(circle);
                log.info("圈子：" + circle.getCircleName() + "的圈子发布次数已添加");
            }
            redisUtils.delete(x);
        }
        Long endTime = System.nanoTime();
        log.info("圈子计数数据同步成功，总耗时:{}",(endTime - startTime) / 1000000 + "ms");
    }
}
