package com.wkk.community.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Time: 2020/5/8上午11:14
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class AlphaJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(Thread.currentThread().getName() + " execute a quartz job");

    }
}
