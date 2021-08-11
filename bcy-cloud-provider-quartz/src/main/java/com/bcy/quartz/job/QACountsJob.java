package com.bcy.quartz.job;

import com.bcy.quartz.mapper.QaMapper;
import com.bcy.quartz.mapper.UserMapper;
import com.bcy.quartz.pojo.Helps;
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
public class QACountsJob implements Job {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private QaMapper qaMapper;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("正在更新问答计数数据");
        Map<Long,Integer> followMap = redisUtils.getAllRedisDataByKeys("followQA");
        Map<Long,Integer> answerMap = redisUtils.getAllRedisDataByKeys("answerQA");
        Long startTime = System.nanoTime();
        for(Long x:followMap.keySet()){
            //获取数据
            Qa qa = qaMapper.selectById(x);
            if(qa != null){
                qa.setFollowCounts(followMap.get(x));
                qaMapper.updateById(qa);
                log.info("问答编号：" + qa.getNumber() + "的问答关注次数已添加");
            }
            redisUtils.delete("followQA_" + x);
        }
        for(Long x:answerMap.keySet()){
            //获取数据
            Qa qa = qaMapper.selectById(x);
            if(qa != null){
                qa.setAnswerCounts(answerMap.get(x));
                qaMapper.updateById(qa);
                log.info("问答编号：" + qa.getNumber() + "的问答回答次数已添加");
            }
            redisUtils.delete("answerQA_" + x);
        }
        Long endTime = System.nanoTime();
        log.info("问答数据同步成功，总耗时:{}",(endTime - startTime) / 1000000 + "ms");
    }

}
