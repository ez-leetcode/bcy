package com.bcy.quartz.job;

import com.bcy.quartz.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DiscussCountsJob implements Job {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }


}
