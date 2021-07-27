package com.bcy.quartz.job;


import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelpSolvedCountsJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext){



    }
}
