package com.wkk.community.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Time: 2020/4/28下午8:08
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Service
public class AlphaService {

    public AlphaService(){
        System.out.println("构造器 alpha");
    }

    @PostConstruct
    // 在构造器之后调用
    public void init(){
        System.out.println("初始化 init");
    }

    @PreDestroy
    public void destory(){
        System.out.println("销毁 destory");
    }




}
