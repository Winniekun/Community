package com.wkk.community;

import com.wkk.community.dao.AlphaDao;
import com.wkk.community.service.AlphaService;
import javafx.application.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class CommunityApplicationTests implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
//        System.out.println(applicationContext);
//        AlphaDao alphaDao = (AlphaDao) applicationContext.getBean(AlphaDao.class);
//        System.out.println(alphaDao.select());

    }

    @Test
    public void beanTestManagment(){
//        AlphaService alphaService = applicationContext.getBean(AlphaService.class);
//        System.out.println(alphaService);
//        AlphaService alphaService2 = applicationContext.getBean(AlphaService.class);
//        System.out.println(alphaService2);

    }

    @Test
    public void testBeanConfig(){
//        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) applicationContext.getBean(SimpleDateFormat.class);
//        System.out.println(simpleDateFormat.format(new Date()));

    }
    @Autowired
    private AlphaDao alphaDao;
    @Autowired
    private AlphaService alphaService;
    @Autowired
    private SimpleDateFormat simpleDateFormat;
    @Test
    public void testAutowired(){
//        System.out.println(alphaDao);
//        System.out.println(alphaService);
//        System.out.println(simpleDateFormat);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
