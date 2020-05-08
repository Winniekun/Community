package com.wkk.community.config;

import com.wkk.community.quartz.AlphaJob;
import com.wkk.community.quartz.PostScoreReferenceJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import javax.xml.soap.Detail;

/**
 * @Time: 2020/5/8上午11:16
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
// 配置 -> 数据库 -> 调用
@Configuration
public class QuartzConfig {
    // factorybean 简化bean的实例化过程
    // 1. 通过factorybean封装了Bean的实例化过程
    // 2. 将factoryBean装配到Spring容器中
    // 3. 将factoryBean注入到其他的bean
    // 4. 该bean得到的是FactoryBean所管理的对象实例


    @Bean
    public JobDetailFactoryBean alphaJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(AlphaJob.class);
        factoryBean.setName("alphaJob");
        factoryBean.setGroup("alphaJobGroup");
        // 任务是否是持久保存
        factoryBean.setDurability(true);
        // 任务是否可恢复
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }

    @Bean
    public SimpleTriggerFactoryBean alphaTrigger(JobDetail alphaJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(alphaJobDetail);
        factoryBean.setName("alphaTrigger");
        factoryBean.setGroup("alphaTriggerGroup");
        factoryBean.setRepeatInterval(3000);
        factoryBean.setJobDataMap(new JobDataMap());

        return factoryBean;
    }

    // 刷新帖子分数任务
    @Bean
    public JobDetailFactoryBean postScoreRefreshJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(PostScoreReferenceJob.class);
        factoryBean.setName("postScoreRefreshJob");
        factoryBean.setGroup("communityJobGroup");
        // 任务是否是持久保存
        factoryBean.setDurability(true);
        // 任务是否可恢复
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }

    @Bean
    public SimpleTriggerFactoryBean postScoreRefreshTrigger(JobDetail postScoreRefreshJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(postScoreRefreshJobDetail);
        factoryBean.setName("postScoreRefreshTrigger");
        factoryBean.setGroup("communityTriggerGroup");
        factoryBean.setRepeatInterval(1000 * 60 * 5);
//        factoryBean.setRepeatInterval(1000 * 10);
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }



}
