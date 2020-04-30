package com.wkk.community.config;

import com.wkk.community.controller.interceptor.AlphaInterceptor;
import com.wkk.community.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Time: 2020/4/30下午4:06
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private AlphaInterceptor alphaInterceptor;
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor)
        .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.jpeg", "/**/*.png")
        .addPathPatterns("/register", "/login");

        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.jpeg", "/**/*.png");
    }
}
