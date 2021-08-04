package com.bcy.quartz.config;

import com.bcy.quartz.job.CosCommentsCountsJob;
import com.bcy.quartz.job.CosCountsJob;
import com.bcy.quartz.job.HelpSolvedCountsJob;
import com.bcy.quartz.job.QACountsJob;
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

    @Bean
    public JobDetail qaCountsJobDetail(){
        //关联业务类
        return JobBuilder.newJob(QACountsJob.class)
                //给JobDetail起名字
                .withIdentity("qaCountsDetail")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger qaCountsTrigger(){
        //十五分钟一刷新
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 */15 * * * ?");
        return TriggerBuilder.newTrigger()
                //关联上述JobDetail
                .forJob(qaCountsJobDetail())
                .withIdentity("qaCountsTrigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail cosCountsJobDetail(){
        //关联业务类
        return JobBuilder.newJob(CosCountsJob.class)
                //给JobDetail起名字
                .withIdentity("cosCountsDetail")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger discussCountsTrigger(){
        //十五分钟一刷新
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 */15 * * * ?");
        return TriggerBuilder.newTrigger()
                //关联上述JobDetail
                .forJob(cosCountsJobDetail())
                .withIdentity("cosCountsTrigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail cosCommentsCountsJobDetail(){
        //关联业务类
        return JobBuilder.newJob(CosCommentsCountsJob.class)
                //给JobDetail起名字
                .withIdentity("cosCommenttsCountsDetail")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger cosCommentsCountsTrigger(){
        //十五分钟一刷新
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 */15 * * * ?");
        return TriggerBuilder.newTrigger()
                //关联上述JobDetail
                .forJob(cosCommentsCountsJobDetail())
                .withIdentity("cosCommentsCountsTrigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

}
