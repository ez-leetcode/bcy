package com.bcy.quartz.job;

import com.bcy.quartz.mapper.UserMapper;
import com.bcy.quartz.pojo.User;
import com.bcy.quartz.pojo.UserMessage;
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
public class UserCountsJob implements Job {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("正在更新用户计数数据");
        Map<Long,Integer> fansMap = redisUtils.getAllRedisDataByKeys("fansCounts");
        Map<Long,Integer> followMap = redisUtils.getAllRedisDataByKeys("followCounts");
        Map<Long,Integer> momentMap = redisUtils.getAllRedisDataByKeys("momentCounts");
        Long startTime = System.nanoTime();
        for(Long x:fansMap.keySet()){
            //获取数据
            User user = userMapper.selectById(x);
            if(user != null){
                user.setFansCounts(fansMap.get(x));
                userMapper.updateById(user);
                log.info("用户：" + user.getId() + "的粉丝计数已更新");
            }
            redisUtils.delete("fansCounts_" + x);
        }
        for(Long x:followMap.keySet()){
            //获取数据
            User user = userMapper.selectById(x);
            if(user != null){
                user.setFollowCounts(followMap.get(x));
                userMapper.updateById(user);
                log.info("用户：" + user.getId() + "的关注计数已更新");
            }
            redisUtils.delete("followCounts_" + x);
        }
        for(Long x:momentMap.keySet()){
            //获取数据
            User user = userMapper.selectById(x);
            if(user != null){
                user.setMomentCounts(momentMap.get(x));
                userMapper.updateById(user);
                log.info("用户：" + user.getId() + "的动态计数已更新");
            }
            redisUtils.delete("momentCounts_" + x);
        }
        Long endTime = System.nanoTime();
        log.info("个人计数数据同步成功，总耗时:{}",(endTime - startTime) / 1000000 + "ms");
    }

}