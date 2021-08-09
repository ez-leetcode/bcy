package com.bcy.quartz.job;

import com.bcy.quartz.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CosRecommendLabelJob implements Job {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<String,Integer> map = redisUtils.getAllStringRedisDataByKeys("recommendLabel");
        for(String x:map.keySet()){
            //移除旧数据
            redisUtils.delete("recommendLabel_" + x);
        }
        List<Map.Entry<String,Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((Comparator.comparingInt(Map.Entry::getValue)));
        for(int i = 1; i <= 20; i++){
            if(map.size() - i < 0){
                break;
            }
            //存入redis
            redisUtils.saveByHoursTime("hotRecommendLabel_" + i , list.get(map.size() - i).getKey(),9999);
            log.info("新推荐关键词：" + list.get(map.size() - i).getKey() + " 编号：" + i);
        }
        log.info("推荐标签更新成功");
    }

}