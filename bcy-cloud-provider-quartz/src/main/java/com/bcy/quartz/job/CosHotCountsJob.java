package com.bcy.quartz.job;

import com.bcy.mq.HotCosMsg;
import com.bcy.quartz.mapper.CosDayHotMapper;
import com.bcy.quartz.mapper.CosMapper;
import com.bcy.quartz.mapper.UserMapper;
import com.bcy.quartz.pojo.Cos;
import com.bcy.quartz.pojo.CosDayHot;
import com.bcy.quartz.pojo.User;
import com.bcy.quartz.service.RabbitmqProducerService;
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
public class CosHotCountsJob implements Job {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CosDayHotMapper cosDayHotMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CosMapper cosMapper;

    @Autowired
    private RabbitmqProducerService rabbitmqProducerService;

    //推送到消息队列待完成

    //删除
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<Long,Integer> likeMap = redisUtils.getAllRedisDataByKeys("cosHotLikeCounts");
        Map<Long,Integer> favorMap = redisUtils.getAllRedisDataByKeys("cosHotFavorCounts");
        //数据哈希表
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        Map<Long,Integer> map = new HashMap<>();
        for(Long x:likeMap.keySet()){
            map.put(x,likeMap.get(x) * 3);
            redisUtils.delete("cosHotLikeCounts_" + x);
        }
        for(Long x:favorMap.keySet()){
            Integer ck = map.get(x);
            if(ck == null){
                map.put(x,favorMap.get(x) * 5);
            }else{
                map.put(x,favorMap.get(x) * 5 + ck);
            }
            redisUtils.delete("cosHotFavorCounts_" + x);
        }
        List<Map.Entry<Long,Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((Comparator.comparingInt(Map.Entry::getValue)));
        for(int i = 1; i <= 10; i++){
            //清空原cos列表
            redisUtils.delete("hotCos" + i);
        }
        for(int i = 1 ; i <= 10; i++){
            if(map.size() - i < 0){
                break;
            }
            cosDayHotMapper.insert(new CosDayHot(null,list.get(map.size() - i).getKey(),i,dateString));
            redisUtils.saveByHoursTime("hotCos" + i,list.get(map.size() - i).getKey().toString(),48);
            log.info("新日榜热门cos编号：" + list.get(map.size() - i).toString());
            if(i == 1){
                //推送最火的
                Cos cos = cosMapper.selectById(list.get(map.size() - i).getKey());
                if(cos != null){
                    User user = userMapper.selectById(cos.getId());
                    if(user != null){
                        rabbitmqProducerService.sendHotCos(new HotCosMsg(cos.getNumber(),cos.getDescription(),user.getUsername()));
                    }
                }
            }
        }
        log.info("日榜热门cos更新成功");
    }

}