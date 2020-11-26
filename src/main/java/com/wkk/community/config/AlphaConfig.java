package com.wkk.community.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @Time: 2020/4/28下午8:24
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Configuration
public class AlphaConfig {
    // 第三方bean的装配
    @Bean
    public SimpleDateFormat simpleDateFormat(){
        String a = "yes";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
