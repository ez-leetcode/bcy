package com.bcy.quartz.config;

import com.bcy.quartz.job.HelpSolvedCountsJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    //帮助解决问题数据刷新任务
    @Bean
    public JobDetail helpSolvedCountsJobDetail(){
        //关联业务类
        return JobBuilder.newJob(HelpSolvedCountsJob.class)
                //给JobDetail起名字
                .withIdentity("helpSolvedCountsDetail")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger helpSolvedCountsTrigger(){
        //十五分钟一刷新
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 */15 * * * ?");
        return TriggerBuilder.newTrigger()
                //关联上述JobDetail
                .forJob(helpSolvedCountsJobDetail())
                .withIdentity("helpSolvedCountsTrigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }


}
