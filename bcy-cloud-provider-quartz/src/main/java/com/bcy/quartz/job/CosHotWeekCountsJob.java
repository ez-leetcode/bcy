package com.bcy.quartz.job;

import com.bcy.quartz.mapper.CosDayHotMapper;
import com.bcy.quartz.mapper.CosMonthHotMapper;
import com.bcy.quartz.pojo.CosDayHot;
import com.bcy.quartz.pojo.CosMonthHot;
import com.bcy.quartz.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class CosHotWeekCountsJob implements Job {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CosMonthHotMapper cosMonthHotMapper;

    //推送到消息队列待完成

    //删除
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<Long,Integer> likeMap = redisUtils.getAllRedisDataByKeys("cosHotLikeWeekCounts");
        Map<Long,Integer> favorMap = redisUtils.getAllRedisDataByKeys("cosHotFavorWeekCounts");
        //数据哈希表
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        Map<Long,Integer> map = new HashMap<>();
        for(Long x:likeMap.keySet()){
            map.put(x,likeMap.get(x) * 3);
            redisUtils.delete("cosHotLikeWeekCounts_" + x);
        }
        for(Long x:favorMap.keySet()){
            Integer ck = map.get(x);
            if(ck == null){
                map.put(x,favorMap.get(x) * 5);
            }else{
                map.put(x,favorMap.get(x) * 5 + ck);
            }
            redisUtils.delete("cosHotFavorWeekCounts_" + x);
        }
        List<Map.Entry<Long,Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((Comparator.comparingInt(Map.Entry::getValue)));
        for(int i = 1; i <= 10; i++){
            //清空原cos列表
            redisUtils.delete("hotWeekCos" + i);
        }
        for(int i = 1 ; i <= 10; i++){
            if(map.size() - i < 0){
                break;
            }
            cosMonthHotMapper.insert(new CosMonthHot(null,list.get(map.size() - i).getKey(),i,dateString));
            redisUtils.saveByHoursTime("hotWeekCos" + i,list.get(map.size() - i).getKey().toString(),24 * 8);
            log.info("新周榜热门cos编号：" + list.get(map.size() - i).toString());
        }
        log.info("周榜热门cos更新成功");
    }

}