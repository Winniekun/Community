package com.wkk.community.threadPool;

import com.wkk.community.CommunityApplication;
import com.wkk.community.service.AlphaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Time: 2020/5/8上午10:16
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class ThreadPoolTest {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTest.class);

    // JDK 普通线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    // JDK 定时执行任务线程池
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    // spring 普通线程池
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    // spring 执行定时任务线程池
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Autowired
    private AlphaService alphaService;

    public void sleep(long m){
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExecutorService(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("hello 线程池");
            }
        };
        for (int i = 0; i < 10; i++) {
            executorService.submit(task);
        }
        sleep(10000);
    }

    // 定时任务
    @Test
    public void testScheduledExecutorService(){
        Runnable tast = new Runnable() {
            @Override
            public void run() {
                logger.error("hello hello 嘿");
            }
        };

        scheduledExecutorService.scheduleAtFixedRate(tast, 10000, 1000, TimeUnit.MILLISECONDS);
        sleep(30000);
    }

    // 普通
    @Test
    public void testSpringThreadPoolTaskExecutor(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.error("spring task ThreadPoolTaskExecutor");
            }
        };

        for (int i = 0; i < 10; i++) {
            taskExecutor.execute(task);
        }
        sleep(10000);
    }

    // 定时
    @Test
    public void testSpringThreadPoolTaskScheduler(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.error("spring testSpringThreadPoolTaskScheduler");
            }
        };

        Date startTime = new Date(System.currentTimeMillis() + 10000);
        threadPoolTaskScheduler.scheduleAtFixedRate(task, startTime, 10000);
        sleep(10000);
    }

    @Test
    public void testPool(){
        for (int i = 0; i < 10; i++) {
            alphaService.say();
        }
        sleep(100000);
    }

    @Test
    public void testSchePool(){
        sleep(10000);
    }
}
