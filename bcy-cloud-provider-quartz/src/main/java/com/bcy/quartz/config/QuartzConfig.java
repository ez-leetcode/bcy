package com.bcy.quartz.config;

import com.bcy.quartz.job.*;
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
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/15 * * * ?");
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
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 1/15 * * * ?");
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
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 2/15 * * * ?");
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
                .withIdentity("cosCommentsCountsDetail")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger cosCommentsCountsTrigger(){
        //十五分钟一刷新
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 3/15 * * * ?");
        return TriggerBuilder.newTrigger()
                //关联上述JobDetail
                .forJob(cosCommentsCountsJobDetail())
                .withIdentity("cosCommentsCountsTrigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail userNoReadCountsJobDetail(){
        //关联业务类
        return JobBuilder.newJob(UserNoReadCountsJob.class)
                //给JobDetail起名字
                .withIdentity("userNoReadCountsDetail")
                .storeDurably()
                .build();
    }


    @Bean
    public Trigger userNoReadCountsTrigger(){
        //十五分钟一刷新
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 4/15 * * * ?");
        return TriggerBuilder.newTrigger()
                //关联上述JobDetail
                .forJob(userNoReadCountsJobDetail())
                .withIdentity("userNoReadCountsTrigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail userCountsJobDetail(){
        //关联业务类
        return JobBuilder.newJob(UserCountsJob.class)
                //给JobDetail起名字
                .withIdentity("userCountsDetail")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger userCountsTrigger(){
        //十五分钟一刷新
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 5/15 * * * ?");
        return TriggerBuilder.newTrigger()
                //关联上述JobDetail
                .forJob(userCountsJobDetail())
                .withIdentity("userCountsTrigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail qaAnswerCountsJobDetail(){
        //关联业务类
        return JobBuilder.newJob(QAAnswerCountsJob.class)
                //给JobDetail起名字
                .withIdentity("qaAnswerCountsDetail")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger qaAnswerCountsTrigger(){
        //十五分钟一刷新
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 6/15 * * * ?");
        return TriggerBuilder.newTrigger()
                //关联上述JobDetail
                .forJob(qaAnswerCountsJobDetail())
                .withIdentity("qaAnswerCountsTrigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail qaAnswerCommentCountsJobDetail(){
        //关联业务类
        return JobBuilder.newJob(QAAnswerCommentCountsJob.class)
                //给JobDetail起名字
                .withIdentity("qaAnswerCommentCountsDetail")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger qaAnswerCommentCountsTrigger(){
        //十五分钟一刷新
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 7/15 * * * ?");
        return TriggerBuilder.newTrigger()
                //关联上述JobDetail
                .forJob(qaAnswerCommentCountsJobDetail())
                .withIdentity("qaAnswerCommentCountsTrigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail cosHotCountsJobDetail(){
        //关联业务类
        return JobBuilder.newJob(CosHotCountsJob.class)
                //给JobDetail起名字
                .withIdentity("cosHotCountsDetail")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger circleCountsTrigger(){
        //十五分钟一刷新
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 8/15 * * * ?");
        return TriggerBuilder.newTrigger()
                //关联上述JobDetail
                .forJob(circleCountsJobDetail())
                .withIdentity("circleCountsTrigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail circleCountsJobDetail(){
        //关联业务类
        return JobBuilder.newJob(CircleCountsJob.class)
                //给JobDetail起名字
                .withIdentity("circleCountsDetail")
                .storeDurably()
                .build();
    }


    @Bean
    public Trigger cosHotCountsTrigger(){
        //十五分钟一刷新
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 0 1/1 * ? *");
        return TriggerBuilder.newTrigger()
                //关联上述JobDetail
                .forJob(cosHotCountsJobDetail())
                .withIdentity("cosHotCountsTrigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail cosRecommendLabelJobDetail(){
        //关联业务类
        return JobBuilder.newJob(CosRecommendLabelJob.class)
                //给JobDetail起名字
                .withIdentity("cosRecommendLabelDetail")
                .storeDurably()
                .build();
    }


    @Bean
    public Trigger cosRecommendLabelTrigger(){
        //十五分钟一刷新
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 0 1/1 * ? *");
        return TriggerBuilder.newTrigger()
                //关联上述JobDetail
                .forJob(cosRecommendLabelJobDetail())
                .withIdentity("cosRecommendLabelTrigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail cosHotWeekCountsJobDetail(){
        //关联业务类
        return JobBuilder.newJob(CosHotWeekCountsJob.class)
                //给JobDetail起名字
                .withIdentity("cosHotWeekCountsDetail")
                .storeDurably()
                .build();
    }


    @Bean
    public Trigger cosHotWeekCountsTrigger(){
        //十五分钟一刷新
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 0 1/7 * ? *");
        return TriggerBuilder.newTrigger()
                //关联上述JobDetail
                .forJob(cosHotWeekCountsJobDetail())
                .withIdentity("cosHotWeekCountsTrigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

}
