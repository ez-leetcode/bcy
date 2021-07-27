package com.bcy.quartz.job;


import com.bcy.quartz.mapper.HelpsMapper;
import com.bcy.quartz.pojo.Helps;
import com.bcy.quartz.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class HelpSolvedCountsJob implements Job {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private HelpsMapper helpsMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext){
        log.info("正在更新帮助的解决数据");
        //获取数据
        Map<Long,Integer> map = redisUtils.getAllRedisDataByKeys("isSolved");
        Map<Long,Integer> map1 = redisUtils.getAllRedisDataByKeys("noSolved");
        Long startTime = System.nanoTime();
        for(Long x:map.keySet()){
            //获取数据
            Helps helps = helpsMapper.selectById(x);
            if(helps != null){
                helps.setSolveCounts(helps.getSolveCounts() + map.get(x));
                helpsMapper.updateById(helps);
                log.info("帮助编号：" + helps.getNumber() + "的解决次数已添加，已增加：" + map.get(x));
            }
            redisUtils.delete("isSolved_" + x);
        }
        for(Long x:map1.keySet()){
            //获取数据
            Helps helps = helpsMapper.selectById(x);
            if(helps != null){
                helps.setSolveCounts(helps.getSolveCounts() + map1.get(x));
                helpsMapper.updateById(helps);
                log.info("帮助编号：" + helps.getNumber() + "的未解决次数已添加，已增加：" + map.get(x));
            }
            redisUtils.delete("noSolved_" + x);
        }
        Long endTime = System.nanoTime();
        log.info("帮助处理数据同步成功，总耗时:{}",(endTime - startTime) / 1000000 + "ms");
    }

}
