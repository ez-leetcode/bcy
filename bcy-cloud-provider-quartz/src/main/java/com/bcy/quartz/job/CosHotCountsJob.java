package com.bcy.quartz.job;

import com.bcy.mq.HotCosMsg;
import com.bcy.quartz.mapper.CosDayHotMapper;
import com.bcy.quartz.mapper.CosPlayMapper;
import com.bcy.quartz.mapper.UserMapper;
import com.bcy.quartz.pojo.CosPlay;
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
    private CosPlayMapper cosPlayMapper;

    @Autowired
    private RabbitmqProducerService rabbitmqProducerService;

    //推送到消息队列待完成

    //删除
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<Long,Integer> likeMap1 = redisUtils.getAllRedisDataByKeys("cosHotLikeCounts1");
        Map<Long,Integer> favorMap1 = redisUtils.getAllRedisDataByKeys("cosHotFavorCounts1");
        Map<Long,Integer> likeMap2 = redisUtils.getAllRedisDataByKeys("cosHotLikeCounts2");
        Map<Long,Integer> favorMap2 = redisUtils.getAllRedisDataByKeys("cosHotFavorCounts2");
        Map<Long,Integer> likeMap3 = redisUtils.getAllRedisDataByKeys("cosHotLikeCounts3");
        Map<Long,Integer> favorMap3 = redisUtils.getAllRedisDataByKeys("cosHotFavorCounts3");
        //数据哈希表
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        Map<Long,Integer> map1 = new HashMap<>();
        Map<Long,Integer> map2 = new HashMap<>();
        Map<Long,Integer> map3 = new HashMap<>();
        for(Long x:likeMap1.keySet()){
            map1.put(x,likeMap1.get(x) * 3);
            redisUtils.delete("cosHotLikeCounts1_" + x);
        }
        for(Long x:likeMap2.keySet()){
            map2.put(x,likeMap2.get(x) * 3);
            redisUtils.delete("cosHotLikeCounts2_" + x);
        }
        for(Long x:likeMap3.keySet()){
            map3.put(x,likeMap3.get(x) * 3);
            redisUtils.delete("cosHotLikeCounts3_" + x);
        }
        for(Long x:favorMap1.keySet()){
            Integer ck = map1.get(x);
            if(ck == null){
                map1.put(x,favorMap1.get(x) * 5);
            }else{
                map1.put(x,favorMap1.get(x) * 5 + ck);
            }
            redisUtils.delete("cosHotFavorCounts1_" + x);
        }
        for(Long x:favorMap2.keySet()){
            Integer ck = map2.get(x);
            if(ck == null){
                map2.put(x,favorMap2.get(x) * 5);
            }else{
                map2.put(x,favorMap2.get(x) * 5 + ck);
            }
            redisUtils.delete("cosHotFavorCounts2_" + x);
        }
        for(Long x:favorMap3.keySet()){
            Integer ck = map3.get(x);
            if(ck == null){
                map3.put(x,favorMap3.get(x) * 5);
            }else{
                map3.put(x,favorMap3.get(x) * 5 + ck);
            }
            redisUtils.delete("cosHotFavorCounts3_" + x);
        }
        List<Map.Entry<Long,Integer>> list1 = new ArrayList<>(map1.entrySet());
        list1.sort((Comparator.comparingInt(Map.Entry::getValue)));

        List<Map.Entry<Long,Integer>> list2 = new ArrayList<>(map2.entrySet());
        list2.sort((Comparator.comparingInt(Map.Entry::getValue)));

        List<Map.Entry<Long,Integer>> list3 = new ArrayList<>(map3.entrySet());
        list3.sort((Comparator.comparingInt(Map.Entry::getValue)));
        for(int i = 1; i <= 10; i++){
            //清空原cos列表
            redisUtils.delete("hotCos1" + i);
            redisUtils.delete("hotCos2" + i);
            redisUtils.delete("hotCos3" + i);
        }
        for(int i = 1 ; i <= 10; i++){
            if(map1.size() - i < 0){
                break;
            }
            cosDayHotMapper.insert(new CosDayHot(null,list1.get(map1.size() - i).getKey(),i,1,dateString));
            redisUtils.saveByHoursTime("hotCos1" + i,list1.get(map1.size() - i).getKey().toString(),48);
            log.info("新日榜热门cos编号：" + list1.get(map1.size() - i).toString());
            if(i == 1){
                //推送最火的
                CosPlay cosPlay = cosPlayMapper.selectById(list1.get(map1.size() - i).getKey());
                if(cosPlay != null){
                    User user = userMapper.selectById(cosPlay.getId());
                    if(user != null){
                        rabbitmqProducerService.sendHotCos(new HotCosMsg(cosPlay.getNumber(), cosPlay.getDescription(),user.getUsername()));
                    }
                }
            }
        }
        for(int i = 1 ; i <= 10; i++){
            if(map2.size() - i < 0){
                break;
            }
            cosDayHotMapper.insert(new CosDayHot(null,list2.get(map2.size() - i).getKey(),i,2,dateString));
            redisUtils.saveByHoursTime("hotCos2" + i,list2.get(map2.size() - i).getKey().toString(),48);
            log.info("新日榜热门绘画编号：" + list2.get(map2.size() - i).toString());
        }
        for(int i = 1 ; i <= 10; i++){
            if(map3.size() - i < 0){
                break;
            }
            cosDayHotMapper.insert(new CosDayHot(null,list3.get(map3.size() - i).getKey(),i,3,dateString));
            redisUtils.saveByHoursTime("hotCos3" + i,list3.get(map3.size() - i).getKey().toString(),48);
            log.info("新日榜热门写作编号：" + list3.get(map3.size() - i).toString());
        }
        log.info("日榜热门cos更新成功");
    }

}